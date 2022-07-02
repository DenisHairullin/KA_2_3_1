package crud.formatters;

import crud.model.Role;
import crud.service.RoleService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class RoleFormatter implements Formatter<Role> {
    private final RoleService roleService;

    public RoleFormatter(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Role parse(String text, Locale locale) throws ParseException {
        try {
            return roleService.getRole(Integer.parseInt(text));
        } catch (NumberFormatException e) {
            throw (ParseException) new ParseException("Error parsing value: " + text, 0).initCause(e);
        }
    }

    @Override
    public String print(Role object, Locale locale) {
        return object.getId().toString();
    }
}
