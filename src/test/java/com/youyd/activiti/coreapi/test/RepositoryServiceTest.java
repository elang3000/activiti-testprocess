package com.youyd.activiti.coreapi.test;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RepositoryServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryServiceTest.class);

    @Rule
    public ActivitiRule acitivitiRule = new ActivitiRule();

    @Test
    public void testRepository(){
        RepositoryService repositoryService = acitivitiRule.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("测试部署资源").addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("my-process1.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        LOGGER.info("deploy = {}",deploy);
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();




        DeploymentBuilder deploymentBuilder2 = repositoryService.createDeployment();
        deploymentBuilder2.name("测试部署资源22").addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("my-process1.bpmn20.xml");
        Deployment deploy2 = deploymentBuilder2.deploy();

        List<Deployment> deployments = deploymentQuery/*.deploymentId(deploy.getId()).singleResult()*/.orderByDeploymenTime().asc().list();


        for (Deployment deployment : deployments) {
            LOGGER.info("deployment = {}",deployment);
        }

        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : list) {
            LOGGER.info("processDefinition={},version={},key-{},id={}",
                    processDefinition,
                    processDefinition.getVersion(),
                    processDefinition.getKey(),
                    processDefinition.getId());
        }

    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testsuspend(){
        RepositoryService repositoryService = acitivitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        LOGGER.info("processDefinition = {}", processDefinition);
        repositoryService.suspendProcessDefinitionById(processDefinition.getId());
        acitivitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCandidateStarter(){
        RepositoryService repositoryService = acitivitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        LOGGER.info("processDefinition = {}", processDefinition);
        repositoryService.addCandidateStarterUser(processDefinition.getId(),"user");
        repositoryService.addCandidateStarterUser(processDefinition.getId(),"groupM");

        List<IdentityLink> identityLinkList = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
        identityLinkList.forEach((identityLink)->LOGGER.info("IdentityLink->{}",identityLink));

        repositoryService.deleteCandidateStarterUser(processDefinition.getId(),"user");
        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(),"groupM");

        List<IdentityLink> identityLinkList1 = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
        identityLinkList1.forEach((identityLink)->LOGGER.info("IdentityLink->{}",identityLink));
    }

}
