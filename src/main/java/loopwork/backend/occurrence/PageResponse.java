package loopwork.backend.occurrence;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int currentPage,
        long totalItems,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {}