package callofproject.dev.service.filterandsearch.controller;

import callofproject.dev.service.filterandsearch.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController
{
    private final SearchService m_searchService;

    public SearchController(SearchService searchService)
    {
        m_searchService = searchService;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("keyword") String keyword, @RequestParam("p") int page)
    {
        return ResponseEntity.ok(m_searchService.search(keyword, page));
    }
}