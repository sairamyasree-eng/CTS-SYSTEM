package com.iispl.components;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SmartTableComponent extends HtmlMacroComponent {

    private static final long serialVersionUID = 1L;

    @Wire private Textbox  mc_txtSearch;
    @Wire private Listbox  mc_cmbFilter;
    @Wire private Div      mc_dateRangePanel;
    @Wire private Datebox  mc_dtFrom;
    @Wire private Datebox  mc_dtTo;
    @Wire private Button   mc_btnClearDate;
    @Wire private Label    mc_lblCount;
    @Wire private Listbox  mc_tblMain;
    @Wire private Listhead mc_tblHead;
    @Wire private Div      mc_pgNumbers;
    @Wire private Button   mc_btnFirst;
    @Wire private Button   mc_btnPrev;
    @Wire private Button   mc_btnNext;
    @Wire private Button   mc_btnLast;
    @Wire private Label    mc_lblPgInfo;

    private List<ColumnDef>              columns          = new ArrayList<>();
    private List<Map<String, Object>>    allData          = new ArrayList<>();
    private List<Map<String, Object>>    filteredData     = new ArrayList<>();
    private String                       filterKey        = null;
    private String                       dateField        = null;
    private int                          pageSize         = 8;
    private Consumer<Map<String,Object>> rowClickListener = null;

    private String    searchText  = "";
    private String    filterValue = "";
    private LocalDate dateFrom    = null;
    private LocalDate dateTo      = null;
    private String    sortCol     = null;
    private boolean   sortAsc     = true;
    private int       currentPage = 1;

    public static class ColumnDef {
        public final String  key;
        public final String  label;
        public final boolean sortable;
        public final boolean mono;
        public java.util.function.BiFunction<Object, Map<String,Object>, String> renderer;

        public ColumnDef(String key, String label, boolean sortable, boolean mono) {
            this.key = key; this.label = label;
            this.sortable = sortable; this.mono = mono;
        }

        public ColumnDef withRenderer(
                java.util.function.BiFunction<Object, Map<String,Object>, String> r) {
            this.renderer = r;
            return this;
        }
    }

    public SmartTableComponent() {
        // DO NOT call compose() here — ZK calls it automatically
    }

    @Override
    public void afterCompose() {
        super.afterCompose();
        // table renders empty until setData() + refresh() is called
    }

    // ── Public API ────────────────────────────────────────────────────────

    public void setColumns(List<ColumnDef> columns) {
        this.columns = columns;
        buildHeaders();
    }

    public void setData(List<Map<String, Object>> data) {
        this.allData = data != null ? data : new ArrayList<>();
        currentPage  = 1;
    }

    public void setFilterKey(String key) {
        this.filterKey = key;
    }

    public void setFilterOptions(List<String[]> options) {
        if (options == null || options.isEmpty()) return;
        mc_cmbFilter.getItems().clear();
        Listitem all = new Listitem("All Status", "");
        all.setParent(mc_cmbFilter);
        for (String[] opt : options) {
            Listitem item = new Listitem(opt[1], opt[0]);
            item.setParent(mc_cmbFilter);
        }
        mc_cmbFilter.setVisible(true);
        mc_cmbFilter.setSelectedIndex(0);
    }

    public void setDateField(String field) {
        this.dateField = field;
        mc_dateRangePanel.setVisible(field != null);
    }

    public void setPageSize(int size) {
        this.pageSize = size;
    }

    public void setRowClickListener(Consumer<Map<String, Object>> listener) {
        this.rowClickListener = listener;
    }

    public void refresh() {
        applyFiltersAndSort();
        renderPage();
    }

    // ── Event listeners ───────────────────────────────────────────────────

    @Listen("onChanging = #mc_txtSearch")
    public void onSearch(InputEvent e) {
        searchText  = e.getValue().trim().toLowerCase();
        currentPage = 1;
        refresh();
    }

    @Listen("onSelect = #mc_cmbFilter")
    public void onFilter() {
        Listitem sel = mc_cmbFilter.getSelectedItem();
        filterValue  = sel != null ? (String) sel.getValue() : "";
        currentPage  = 1;
        refresh();
    }

    // FIX: split into two separate @Listen methods — ZK does not allow
    // multiple event=selector pairs in a single @Listen annotation
    @Listen("onChange = #mc_dtFrom")
    public void onDateFromChange() {
        onDateChange();
    }

    @Listen("onChange = #mc_dtTo")
    public void onDateToChange() {
        onDateChange();
    }

    private void onDateChange() {
        dateFrom = mc_dtFrom.getValue() != null
            ? mc_dtFrom.getValue().toInstant()
                       .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            : null;
        dateTo = mc_dtTo.getValue() != null
            ? mc_dtTo.getValue().toInstant()
                     .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            : null;
        currentPage = 1;
        refresh();
    }

    @Listen("onClick = #mc_btnClearDate")
    public void clearDateRange() {
        mc_dtFrom.setValue(null);
        mc_dtTo.setValue(null);
        dateFrom    = null;
        dateTo      = null;
        currentPage = 1;
        refresh();
    }

    @Listen("onClick = #mc_btnFirst") public void goFirst() { currentPage = 1; renderPage(); }
    @Listen("onClick = #mc_btnLast")  public void goLast()  { currentPage = totalPages(); renderPage(); }
    @Listen("onClick = #mc_btnPrev")  public void goPrev()  { if (currentPage > 1) { currentPage--; renderPage(); } }
    @Listen("onClick = #mc_btnNext")  public void goNext()  { if (currentPage < totalPages()) { currentPage++; renderPage(); } }

    // ── Internal: build headers ───────────────────────────────────────────

    private void buildHeaders() {
        mc_tblHead.getChildren().clear();
        for (ColumnDef col : columns) {
            Listheader hdr = new Listheader(col.label);
            if (col.sortable) {
                hdr.addEventListener("onClick", e -> {
                    if (col.key.equals(sortCol)) sortAsc = !sortAsc;
                    else { sortCol = col.key; sortAsc = true; }
                    currentPage = 1;
                    refresh();
                });
                hdr.setSclass("sortable-col");
            }
            hdr.setParent(mc_tblHead);
        }
    }

    // ── Internal: filter + sort ───────────────────────────────────────────

    private void applyFiltersAndSort() {
        filteredData = allData.stream()
            .filter(this::matchesSearch)
            .filter(this::matchesFilter)
            .filter(this::matchesDateRange)
            .collect(Collectors.toList());

        if (sortCol != null) {
            filteredData.sort((a, b) -> {
                String av = String.valueOf(a.getOrDefault(sortCol, ""));
                String bv = String.valueOf(b.getOrDefault(sortCol, ""));
                int cmp;
                try {
                    cmp = Double.compare(Double.parseDouble(av), Double.parseDouble(bv));
                } catch (NumberFormatException ex) {
                    cmp = av.compareToIgnoreCase(bv);
                }
                return sortAsc ? cmp : -cmp;
            });
        }
    }

    private boolean matchesSearch(Map<String, Object> row) {
        if (searchText.isEmpty()) return true;
        return columns.stream()
            .filter(c -> c.renderer == null)
            .anyMatch(c -> String.valueOf(row.getOrDefault(c.key, ""))
                               .toLowerCase().contains(searchText));
    }

    private boolean matchesFilter(Map<String, Object> row) {
        if (filterKey == null || filterValue.isEmpty()) return true;
        return filterValue.equalsIgnoreCase(String.valueOf(row.getOrDefault(filterKey, "")));
    }

    private boolean matchesDateRange(Map<String, Object> row) {
        if (dateField == null) return true;
        String raw = String.valueOf(row.getOrDefault(dateField, ""));
        if (raw.isEmpty()) return true;
        try {
            LocalDate d = LocalDate.parse(raw, DateTimeFormatter.ISO_LOCAL_DATE);
            if (dateFrom != null && d.isBefore(dateFrom)) return false;
            if (dateTo   != null && d.isAfter(dateTo))    return false;
        } catch (Exception ignored) {}
        return true;
    }

    // ── Internal: render current page ────────────────────────────────────

    private void renderPage() {
        mc_tblMain.getItems().clear();
        mc_pgNumbers.getChildren().clear();

        int total = filteredData.size();
        int pages = totalPages();
        int start = (currentPage - 1) * pageSize;
        int end   = Math.min(start + pageSize, total);

        mc_lblCount.setValue(total + " record" + (total != 1 ? "s" : ""));

        if (total == 0) {
            Listitem empty = new Listitem();
            Listcell cell  = new Listcell("No records found.");
            cell.setSpan(columns.size());
            cell.setSclass("tbl-empty-cell");
            cell.setParent(empty);
            empty.setParent(mc_tblMain);
        } else {
            for (int i = start; i < end; i++) {
                buildRow(filteredData.get(i)).setParent(mc_tblMain);
            }
        }

        renderPaginationNumbers(pages);
        mc_btnFirst.setDisabled(currentPage == 1);
        mc_btnPrev.setDisabled(currentPage == 1);
        mc_btnNext.setDisabled(currentPage >= pages);
        mc_btnLast.setDisabled(currentPage >= pages);
        mc_lblPgInfo.setValue("Page " + currentPage + " / " + pages);
    }

    private Listitem buildRow(Map<String, Object> row) {
        Listitem item = new Listitem();
        item.setSclass("tbl-row");

        for (ColumnDef col : columns) {
            Object   val  = row.getOrDefault(col.key, "");
            Listcell cell = new Listcell();
            if (col.renderer != null) {
                org.zkoss.zul.Html html = new org.zkoss.zul.Html();
                html.setContent(col.renderer.apply(val, row));
                html.setParent(cell);
            } else {
                cell.setLabel(String.valueOf(val));
                if (col.mono) cell.setSclass("td-mono");
            }
            cell.setParent(item);
        }

        if (rowClickListener != null) {
            item.addEventListener("onClick", e -> rowClickListener.accept(row));
            item.setSclass("tbl-row clickable");
        }
        return item;
    }

    private void renderPaginationNumbers(int pages) {
        for (int i = 1; i <= pages; i++) {
            if (pages > 7 && Math.abs(i - currentPage) > 1 && i != 1 && i != pages) {
                if (Math.abs(i - currentPage) == 2) {
                    Label dots = new Label("…");
                    dots.setSclass("pg-dots");
                    dots.setParent(mc_pgNumbers);
                }
                continue;
            }
            final int page = i;
            Button btn = new Button(String.valueOf(i));
            btn.setSclass(i == currentPage ? "pg-btn active" : "pg-btn");
            btn.addEventListener("onClick", e -> { currentPage = page; renderPage(); });
            btn.setParent(mc_pgNumbers);
        }
    }

    private int totalPages() {
        return Math.max(1, (int) Math.ceil((double) filteredData.size() / pageSize));
    }

    public int    getCurrentPage()   { return currentPage; }
    public int    getTotalRecords()  { return filteredData.size(); }
    public String getSearchText()    { return searchText; }
}