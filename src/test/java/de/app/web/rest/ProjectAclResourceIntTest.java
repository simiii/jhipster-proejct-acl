package de.app.web.rest;

import de.app.DefaultApp;

import de.app.domain.ProjectAcl;
import de.app.repository.ProjectAclRepository;
import de.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static de.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.app.domain.enumeration.ProjectRole;
/**
 * Test class for the ProjectAclResource REST controller.
 *
 * @see ProjectAclResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DefaultApp.class)
public class ProjectAclResourceIntTest {

    private static final ProjectRole DEFAULT_ROLE = ProjectRole.PROJECT_USER;
    private static final ProjectRole UPDATED_ROLE = ProjectRole.PROJECT_ADMIN;

    @Autowired
    private ProjectAclRepository projectAclRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectAclMockMvc;

    private ProjectAcl projectAcl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectAclResource projectAclResource = new ProjectAclResource(projectAclRepository);
        this.restProjectAclMockMvc = MockMvcBuilders.standaloneSetup(projectAclResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectAcl createEntity(EntityManager em) {
        ProjectAcl projectAcl = new ProjectAcl()
            .role(DEFAULT_ROLE);
        return projectAcl;
    }

    @Before
    public void initTest() {
        projectAcl = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectAcl() throws Exception {
        int databaseSizeBeforeCreate = projectAclRepository.findAll().size();

        // Create the ProjectAcl
        restProjectAclMockMvc.perform(post("/api/project-acls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectAcl)))
            .andExpect(status().isCreated());

        // Validate the ProjectAcl in the database
        List<ProjectAcl> projectAclList = projectAclRepository.findAll();
        assertThat(projectAclList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectAcl testProjectAcl = projectAclList.get(projectAclList.size() - 1);
        assertThat(testProjectAcl.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createProjectAclWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectAclRepository.findAll().size();

        // Create the ProjectAcl with an existing ID
        projectAcl.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectAclMockMvc.perform(post("/api/project-acls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectAcl)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectAcl in the database
        List<ProjectAcl> projectAclList = projectAclRepository.findAll();
        assertThat(projectAclList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectAclRepository.findAll().size();
        // set the field null
        projectAcl.setRole(null);

        // Create the ProjectAcl, which fails.

        restProjectAclMockMvc.perform(post("/api/project-acls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectAcl)))
            .andExpect(status().isBadRequest());

        List<ProjectAcl> projectAclList = projectAclRepository.findAll();
        assertThat(projectAclList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectAcls() throws Exception {
        // Initialize the database
        projectAclRepository.saveAndFlush(projectAcl);

        // Get all the projectAclList
        restProjectAclMockMvc.perform(get("/api/project-acls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectAcl.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }
    
    @Test
    @Transactional
    public void getProjectAcl() throws Exception {
        // Initialize the database
        projectAclRepository.saveAndFlush(projectAcl);

        // Get the projectAcl
        restProjectAclMockMvc.perform(get("/api/project-acls/{id}", projectAcl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectAcl.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectAcl() throws Exception {
        // Get the projectAcl
        restProjectAclMockMvc.perform(get("/api/project-acls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectAcl() throws Exception {
        // Initialize the database
        projectAclRepository.saveAndFlush(projectAcl);

        int databaseSizeBeforeUpdate = projectAclRepository.findAll().size();

        // Update the projectAcl
        ProjectAcl updatedProjectAcl = projectAclRepository.findById(projectAcl.getId()).get();
        // Disconnect from session so that the updates on updatedProjectAcl are not directly saved in db
        em.detach(updatedProjectAcl);
        updatedProjectAcl
            .role(UPDATED_ROLE);

        restProjectAclMockMvc.perform(put("/api/project-acls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjectAcl)))
            .andExpect(status().isOk());

        // Validate the ProjectAcl in the database
        List<ProjectAcl> projectAclList = projectAclRepository.findAll();
        assertThat(projectAclList).hasSize(databaseSizeBeforeUpdate);
        ProjectAcl testProjectAcl = projectAclList.get(projectAclList.size() - 1);
        assertThat(testProjectAcl.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectAcl() throws Exception {
        int databaseSizeBeforeUpdate = projectAclRepository.findAll().size();

        // Create the ProjectAcl

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectAclMockMvc.perform(put("/api/project-acls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectAcl)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectAcl in the database
        List<ProjectAcl> projectAclList = projectAclRepository.findAll();
        assertThat(projectAclList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProjectAcl() throws Exception {
        // Initialize the database
        projectAclRepository.saveAndFlush(projectAcl);

        int databaseSizeBeforeDelete = projectAclRepository.findAll().size();

        // Get the projectAcl
        restProjectAclMockMvc.perform(delete("/api/project-acls/{id}", projectAcl.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectAcl> projectAclList = projectAclRepository.findAll();
        assertThat(projectAclList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectAcl.class);
        ProjectAcl projectAcl1 = new ProjectAcl();
        projectAcl1.setId(1L);
        ProjectAcl projectAcl2 = new ProjectAcl();
        projectAcl2.setId(projectAcl1.getId());
        assertThat(projectAcl1).isEqualTo(projectAcl2);
        projectAcl2.setId(2L);
        assertThat(projectAcl1).isNotEqualTo(projectAcl2);
        projectAcl1.setId(null);
        assertThat(projectAcl1).isNotEqualTo(projectAcl2);
    }
}
