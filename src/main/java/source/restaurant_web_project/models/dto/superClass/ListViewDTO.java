package source.restaurant_web_project.models.dto.superClass;


public abstract class ListViewDTO {
    private long currentPage;
    private long totalPages;
    private long totalEntities;

    public ListViewDTO(long currentPage, long totalPages, long totalEntities) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalEntities = totalEntities;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalEntities() {
        return totalEntities;
    }

    public void setTotalEntities(long totalEntities) {
        this.totalEntities = totalEntities;
    }
}
