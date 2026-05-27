package com.iispl.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.iispl.dao.RoleDao;
import com.iispl.entity.Role;
import com.iispl.entity.User;
import com.iispl.enums.Status;
import com.iispl.service.UserServiceImpl;

public class AdminUsersController extends SelectorComposer<Component> {

    @Wire private Rows     userRows;
    @Wire private Label    lblTableTitle;
    @Wire private Label    lblTableCount;
    @Wire private Label    lblTablePager;
    @Wire private Textbox  txtSearch;
    @Wire private Combobox cmbRoleFilter;

    @Wire private Label lblStatAdmin;
    @Wire private Label lblStatMakerOut;
    @Wire private Label lblStatCheckerOut;
    @Wire private Label lblStatMakerIn;
    @Wire private Label lblStatCheckerIn;

    // ── Modal components — wired manually in doAfterCompose ───────────────
    private Window   winUserForm;
    private Textbox  txtUsername;
    private Textbox  txtFullName;
    private Textbox  txtBranch;
    private Textbox  txtPassword;
    private Combobox cmbRole;
    private Combobox cmbStatus;
    private Div      divRoleInfo;
    private Label    lblRoleInfo;
    private Label    lblFormError;
    private Button   btnSaveUser;
    private Button   btnCancelUser;
    private Button   btnCloseForm;

    private final UserServiceImpl   userService = new UserServiceImpl();
    private final RoleDao           roleDao     = new RoleDao();
    private static final java.util.Map<String,String> ROLE_DESC = new java.util.LinkedHashMap<>();
    static {
        ROLE_DESC.put("ADMIN",                       "Administrator — Full system access");
        ROLE_DESC.put("MAKER_OUTWARD",               "Maker Outward — Scan and repair outward cheques");
        ROLE_DESC.put("CHECKER_OUTWARD_VERIFICATION","Checker Outward — Verify outward cheque batches");
        ROLE_DESC.put("MAKER_INWARD",                "Maker Inward — Process inward clearing cheques");
        ROLE_DESC.put("CHECKER_INWARD_VERIFICATION", "Checker Inward — Verify inward cheque batches");
    }

    private final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String editingUserId = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        // Window is inside the vlayout (descendant of borderlayout/comp).
        // Search the full descendant tree from comp.
        winUserForm = findById(comp, "winUserForm", Window.class);
        if (winUserForm != null) {
            txtUsername  = findById(winUserForm, "txtUsername",  Textbox.class);
            txtFullName  = findById(winUserForm, "txtFullName",  Textbox.class);
            txtBranch    = findById(winUserForm, "txtBranch",    Textbox.class);
            txtPassword  = findById(winUserForm, "txtPassword",  Textbox.class);
            cmbRole      = findById(winUserForm, "cmbRole",      Combobox.class);
            cmbStatus    = findById(winUserForm, "cmbStatus",    Combobox.class);
            divRoleInfo  = findById(winUserForm, "divRoleInfo",  Div.class);
            lblRoleInfo  = findById(winUserForm, "lblRoleInfo",  Label.class);
            lblFormError = findById(winUserForm, "lblFormError", Label.class);
            btnSaveUser  = findById(winUserForm, "btnSaveUser",  Button.class);
            btnCancelUser= findById(winUserForm, "btnCancelUser",Button.class);
            btnCloseForm = findById(winUserForm, "btnCloseForm", Button.class);

            // Wire button listeners manually since @Listen won't work for these
            if (btnSaveUser   != null) btnSaveUser.addEventListener("onClick",   e -> onSaveUser());
            if (btnCancelUser != null) btnCancelUser.addEventListener("onClick",  e -> winUserForm.setVisible(false));
            if (btnCloseForm  != null) btnCloseForm.addEventListener("onClick",   e -> winUserForm.setVisible(false));
            if (cmbRole       != null) cmbRole.addEventListener("onSelect",       e -> onRoleSelected());
        }

        populateRoleFilterCombo();
        loadUsers(null, null);
    }

    /**
     * Depth-first search through the component tree to find a component by ID.
     */
    @SuppressWarnings("unchecked")
    private <T extends Component> T findById(Component root, String id, Class<T> type) {
        if (id.equals(root.getId())) {
            return type.isInstance(root) ? (T) root : null;
        }
        for (Component child : root.getChildren()) {
            T found = findById(child, id, type);
            if (found != null) return found;
        }
        return null;
    }

    private void populateRoleFilterCombo() {
        cmbRoleFilter.getChildren().clear();
        Comboitem all = new Comboitem("All Roles");
        all.setValue("ALL");
        cmbRoleFilter.appendChild(all);
        cmbRoleFilter.setSelectedItem(all);
        for (Role r : roleDao.findAll()) {
            Comboitem item = new Comboitem(r.getRoleName());
            item.setValue(r.getId());
            cmbRoleFilter.appendChild(item);
        }
    }

    private void loadUsers(String search, String roleId) {
        List<User> users = userService.getAllUsers();

        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            users = users.stream()
                .filter(u -> u.getUsername().toLowerCase().contains(q)
                          || (u.getFullName() != null && u.getFullName().toLowerCase().contains(q))
                          || (u.getBranch()   != null && u.getBranch().toLowerCase().contains(q)))
                .collect(Collectors.toList());
        }

        if (roleId != null && !roleId.equals("ALL")) {
            users = users.stream()
                .filter(u -> u.getRole() != null && u.getRole().getId().equals(roleId))
                .collect(Collectors.toList());
        }

        updateStats();

        long active   = users.stream().filter(u -> u.getStatus() == Status.Active).count();
        long inactive = users.stream().filter(u -> u.getStatus() == Status.Inactive).count();

        long total = active + inactive;
        lblTableTitle.setValue("SYSTEM USERS (" + total + ")");
        lblTableCount.setValue(active + " active · " + inactive + " inactive");
        lblTablePager.setValue(users.size() + "/" + total);

        userRows.getChildren().clear();

        for (User user : users) {
            Row row = new Row();

            row.appendChild(new Label(user.getUsername()));
           // row.appendChild(new Label(user.getFullName() != null ? user.getFullName() : ""));

            if (user.getRole() != null) {
            	Label badge = new Label(user.getRole().getRoleName());
            	row.appendChild(badge);
            } else {
                row.appendChild(new Label(""));
            }

            row.appendChild(new Label(user.getBranch() != null ? user.getBranch() : ""));

            Label statusLbl = new Label(user.getStatus().name().toUpperCase());
            statusLbl.setSclass(user.getStatus() == Status.Active ? "badge-active" : "badge-inactive");
            row.appendChild(statusLbl);

            row.appendChild(new Label(user.getCreatedDate() != null
                    ? user.getCreatedDate().format(DATE_FMT) : ""));

            Hlayout actions = new Hlayout();
            actions.setSpacing("6px");

            Button btnEdit = new Button("Edit");
            btnEdit.setSclass("btn-edit");
            btnEdit.addEventListener("onClick", e -> openEditForm(user.getId()));

            Button btnToggle = new Button(
                    user.getStatus() == Status.Active ? "Deactivate" : "Activate");
            btnToggle.setSclass(
                    user.getStatus() == Status.Active ? "btn-deactivate" : "btn-activate");
            btnToggle.addEventListener("onClick", e -> toggleStatus(user.getId()));

            actions.appendChild(btnEdit);
            actions.appendChild(btnToggle);
            row.appendChild(actions);

            userRows.appendChild(row);
        }
    }

    private void updateStats() {
        List<User> all = userService.getAllUsers();
        Map<String, Long> byRole = all.stream()
            .filter(u -> u.getRole() != null)
            .collect(Collectors.groupingBy(
                u -> u.getRole().getRoleName().toUpperCase(), Collectors.counting()));

        lblStatAdmin.setValue(String.valueOf(byRole.getOrDefault("ADMIN", 0L)));
        lblStatMakerOut.setValue(String.valueOf(byRole.getOrDefault("MAKER_OUTWARD", 0L)));
        lblStatCheckerOut.setValue(String.valueOf(byRole.getOrDefault("CHECKER_OUTWARD", 0L)));
        lblStatMakerIn.setValue(String.valueOf(byRole.getOrDefault("MAKER_INWARD", 0L)));
        lblStatCheckerIn.setValue(String.valueOf(byRole.getOrDefault("CHECKER_INWARD", 0L)));
    }

    private String roleBadgeClass(String roleName) {
        if (roleName == null) return "";
        switch (roleName.toUpperCase()) {
            case "ADMIN":           return "badge-role-admin";
            case "MAKER_OUTWARD":   return "badge-role-maker-out";
            case "CHECKER_OUTWARD": return "badge-role-checker-out";
            case "MAKER_INWARD":    return "badge-role-maker-in";
            case "CHECKER_INWARD":  return "badge-role-checker-in";
            default:                return "badge-role-default";
        }
    }

    @Listen("onChanging = #txtSearch; onChange = #txtSearch")
    public void onSearch() { applyFilters(); }

    @Listen("onSelect = #cmbRoleFilter")
    public void onRoleFilter() { applyFilters(); }

    private void applyFilters() {
        String search = txtSearch.getValue();
        String roleId = cmbRoleFilter.getSelectedItem() != null
                ? (String) cmbRoleFilter.getSelectedItem().getValue() : "ALL";
        loadUsers(search, roleId);
    }

    @Listen("onAddUser = #pgHeader")
    public void onAddUser() {
        editingUserId = null;
        if (winUserForm != null) winUserForm.setTitle("ADD NEW USER");
        clearForm();
        populateRoleCombo(null);
        setStatusCombo("Active");
        if (winUserForm != null) winUserForm.setVisible(true);
    }

    public void onRoleSelected() {
        if (cmbRole == null) return;
        Comboitem sel = cmbRole.getSelectedItem();
        if (sel != null) {
            String roleName = sel.getLabel().toUpperCase().replace(" ", "_");
            String desc = ROLE_DESC.getOrDefault(roleName, "");
            if (!desc.isEmpty()) {
                if (lblRoleInfo  != null) lblRoleInfo.setValue("Role: " + desc);
                if (divRoleInfo  != null) divRoleInfo.setVisible(true);
            } else {
                if (divRoleInfo  != null) divRoleInfo.setVisible(false);
            }
        } else {
            if (divRoleInfo != null) divRoleInfo.setVisible(false);
        }
    }

    private void openEditForm(String userId) {
        User user = userService.findById(userId);
        if (user == null) return;
        editingUserId = userId;
        if (winUserForm  != null) winUserForm.setTitle("EDIT USER");
        if (txtUsername  != null) txtUsername.setValue(user.getUsername());
        if (txtFullName  != null) txtFullName.setValue(user.getFullName() != null ? user.getFullName() : "");
        if (txtBranch    != null) txtBranch.setValue(user.getBranch() != null ? user.getBranch() : "");
        if (txtPassword  != null) txtPassword.setValue("");
        populateRoleCombo(user.getRole() != null ? user.getRole().getId() : null);
        setStatusCombo(user.getStatus().name());
        if (lblFormError != null) lblFormError.setVisible(false);
        if (winUserForm  != null) winUserForm.setVisible(true);
    }

    private void setStatusCombo(String statusName) {
        if (cmbStatus == null) return;
        for (int i = 0; i < cmbStatus.getItemCount(); i++) {
            Comboitem item = cmbStatus.getItemAtIndex(i);
            if (item.getValue().toString().equalsIgnoreCase(statusName)) {
                cmbStatus.setSelectedItem(item);
                return;
            }
        }
    }

    private void populateRoleCombo(String selectedRoleId) {
        if (cmbRole == null) return;
        cmbRole.getChildren().clear();
        for (Role r : roleDao.findAll()) {
            Comboitem item = new Comboitem(r.getRoleName());
            item.setValue(r.getId());
            cmbRole.appendChild(item);
            if (r.getId().equals(selectedRoleId)) {
                cmbRole.setSelectedItem(item);
            }
        }
    }

    private void toggleStatus(String userId) {
        userService.toggleStatus(userId);
        applyFilters();
    }

    public void onSaveUser() {
        if (txtUsername == null || txtFullName == null || txtBranch == null
                || txtPassword == null || cmbRole == null) return;

        String username   = txtUsername.getValue().trim();
        String fullName   = txtFullName.getValue().trim();
        String branch     = txtBranch.getValue().trim();
        String password   = txtPassword.getValue().trim();
        Comboitem selRole = cmbRole.getSelectedItem();

        if (username.isEmpty() || fullName.isEmpty() || branch.isEmpty() || selRole == null) {
            showFormError("Username, full name, branch and role are required.");
            return;
        }
        if (editingUserId == null && password.isEmpty()) {
            showFormError("Password is required for new users.");
            return;
        }

        try {
            String roleId = (String) selRole.getValue();
            if (editingUserId == null) {
                userService.addUser(username, password, fullName, branch, roleId);
            } else {
                userService.updateUser(editingUserId, fullName, branch, roleId,
                        password.isEmpty() ? null : password);
            }
            if (winUserForm != null) winUserForm.setVisible(false);
            applyFilters();
        } catch (IllegalArgumentException e) {
            showFormError(e.getMessage());
        }
    }

    private void clearForm() {
        if (txtUsername  != null) txtUsername.setValue("");
        if (txtFullName  != null) txtFullName.setValue("");
        if (txtBranch    != null) txtBranch.setValue("");
        if (txtPassword  != null) txtPassword.setValue("");
        if (cmbRole      != null) cmbRole.setSelectedItem(null);
        if (lblFormError != null) lblFormError.setVisible(false);
        if (divRoleInfo  != null) divRoleInfo.setVisible(false);
    }

    private void showFormError(String msg) {
        if (lblFormError == null) return;
        lblFormError.setValue(msg);
        lblFormError.setVisible(true);
    }
}