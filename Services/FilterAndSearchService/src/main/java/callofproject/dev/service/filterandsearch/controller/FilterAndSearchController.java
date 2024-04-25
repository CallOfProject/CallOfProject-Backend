package callofproject.dev.service.filterandsearch.controller;

import callofproject.dev.service.filterandsearch.dto.ProjectFilterDTO;
import callofproject.dev.service.filterandsearch.service.FilterService;
import callofproject.dev.service.filterandsearch.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/filter-and-search/service")
public class FilterAndSearchController
{
    private final FilterService m_filterService;
    private final SearchService m_searchService;

    public FilterAndSearchController(FilterService filterService, SearchService searchService)
    {
        m_filterService = filterService;
        m_searchService = searchService;
    }

    @PostMapping("/filter/projects")
    public ResponseEntity<Object> filterProjects(@RequestBody ProjectFilterDTO filterDTO, @RequestParam("p") int page)
    {
        return ResponseEntity.ok(m_filterService.filterProjects(filterDTO, page));
    }

    @PostMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("keyword") String keyword, @RequestParam("p") int page)
    {
        return ResponseEntity.ok(m_searchService.search(keyword, page));
    }
}