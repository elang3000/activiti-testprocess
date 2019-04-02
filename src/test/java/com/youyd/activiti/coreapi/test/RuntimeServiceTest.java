package com.youyd.activiti.coreapi.test;

import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class RuntimeServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeServiceTest.class);

    @Rule
    public ActivitiRule acitivitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcess(){
        RuntimeService runtimeService = acitivitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        variables.put("key2", "value2");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        LOGGER.info("processInstance={}",processInstance);
    }

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testProcessInstanceBuilder(){
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        variables.put("key2", "value2");
        RuntimeService runtimeService = acitivitiRule.getRuntimeService();

        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();

        ProcessInstance processInstance = processInstanceBuilder.businessKey("businessKey001").processDefinitionKey("my-process").variables(variables).start();

        LOGGER.info("businessKey001={}",processInstance);

        runtimeService.setVariable(processInstance.getId(),"key3","value3");
        runtimeService.setVariable(processInstance.getId(),"key2","value5");
        Map<String, Object> variables1 = runtimeService.getVariables(processInstance.getId());

        LOGGER.info("variables1->{}",variables1);
    }

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcessQuery(){
        RuntimeService runtimeService = acitivitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        LOGGER.info("processInstance={}",processInstance);
        ProcessInstance processInstance1 = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();

    }


    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testExecutionQuery(){
        RuntimeService runtimeService = acitivitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        LOGGER.info("processInstance={}",processInstance);
        List<Execution> executions = runtimeService.createExecutionQuery().listPage(0, 100);
        executions.forEach(execution->LOGGER.info("啊啊啊啊:{}",execution));
    }

}
