package com.wegether.app.service.project;

import com.wegether.app.dao.FileDAO;
import com.wegether.app.dao.ProjectDAO;
import com.wegether.app.dao.ProjectFileDAO;
import com.wegether.app.domain.dto.ProjectDTO;
import com.wegether.app.domain.dto.*;
import com.wegether.app.domain.vo.*;
import com.wegether.app.domain.dto.ProjectPagination;
import com.wegether.app.domain.type.FileType;
import com.wegether.app.domain.vo.ProjectFileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectDAO projectDAO;
    private final FileDAO fileDAO;
    private final ProjectFileDAO projectFileDAO;
    private final FileVO fileVO;
    private final ProjectFileDTO projectFileDTO;


//    @Override
//    public List<ProjectDTO> getList(ProjectPagination projectPagination) {
//        return projectDAO.projectFindAll(projectPagination);
//    }
//
//    @Override
//    public void write(ProjectDTO projectDTO) {
//        projectDAO.projectSave(projectDTO);
//    }


//    @Override
//    public Optional<ProjectDTO> getProject(Long id) {
//        return projectDAO.findById(id);
//    }

//    @Override
//    public void modify(ProjectDTO projectDTO) {
//        projectDAO.setProjectDTO(toDTO(projectDTO));
//    }
//
//    @Override
//    public void remove(Long id) {
//        projectDAO.ProjectDelete(id);
//    }
//
//    @Override
//    public int getTotal() {
//        return projectDAO.findCountOfProject();
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ProjectDTO> getList(ProjectPagination projectPagination) {
        final List<ProjectDTO> projectS = projectDAO.projectFindAll(projectPagination);
        projectS.forEach(project -> project.setFiles(fileDAO.projectFindAll(project.getId())));
        return projectS;
    }

    //    자료 등록 - 파일
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void write(ProjectDTO projectDTO) {
        projectDAO.projectSave(projectDTO);
        for(int i=0; i<projectDTO.getFiles().size(); i++){
            projectDTO.getFiles().get(i).setProjectId(projectDTO.getId());
            projectDTO.getFiles().get(i).setFileType(i == 0 ? FileType.REPRESENTATIVE.name() : FileType.NON_REPRESENTATIVE.name());
            fileDAO.save(projectDTO.getFiles().get(i));
        }
        projectDTO.getFiles().forEach(projectFileDTO ->
        { ProjectFileVO projectFileVO = new ProjectFileVO();
            projectFileVO.setId(projectFileDTO.getId());
            projectFileVO.setProjectId(projectFileDTO.getProjectId());
            projectFileDAO.save(projectFileVO);
        });
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<ProjectDTO> read(Long id) {
        final Optional<ProjectDTO> foundProject = projectDAO.findById(id);
        if (foundProject.isPresent()) {
            foundProject.get().setFiles(fileDAO.projectFindAll(foundProject.get().getId()));
        }
        return foundProject;
    }

    @Override
    public int getTotal() {
        return projectDAO.findCountOfProject();
    }


}
