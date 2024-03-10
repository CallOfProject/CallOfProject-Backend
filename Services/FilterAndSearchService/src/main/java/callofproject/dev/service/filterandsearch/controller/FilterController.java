package callofproject.dev.service.filterandsearch.controller;

import callofproject.dev.service.filterandsearch.dto.ProjectFilterDTO;
import callofproject.dev.service.filterandsearch.service.FilterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/filter")
public class FilterController
{
    private final FilterService m_filterService;

    public FilterController(FilterService filterService)
    {
        m_filterService = filterService;
    }

    @GetMapping("/projects")
    public ResponseEntity<Object> filterProjects(@RequestBody ProjectFilterDTO filterDTO, @RequestParam("p") int page)
    {
        return ResponseEntity.ok(m_filterService.filterProjects(filterDTO, page));
    }
}