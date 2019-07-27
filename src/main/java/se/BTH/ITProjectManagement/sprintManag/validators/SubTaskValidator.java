package se.BTH.ITProjectManagement.sprintManag.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import se.BTH.ITProjectManagement.sprintManag.models.SubTask;
import se.BTH.ITProjectManagement.sprintManag.repositories.SubTaskRepository;

@Component
public class SubTaskValidator implements Validator {
    @Autowired
    private SubTaskRepository subTaskRepo;

    @Override
    public boolean supports(Class<?> aClass) {
        return SubTask.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SubTask subTask = (SubTask) o;

        if (subTask.getOEstimate() < 0 ) {
            errors.rejectValue("OEstimate", "value.subtaskAttr.OEstimate");
        }

    }
}
