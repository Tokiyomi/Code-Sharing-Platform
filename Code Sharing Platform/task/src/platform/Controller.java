package platform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@CrossOrigin
public class Controller {

    //@Autowired
    //Code code = new Code();

    @Autowired
    CodeService service;

    private List<Code> code_list = new ArrayList<>();//Arrays.asList(code)


    /*@GetMapping(path="/code")
    public ResponseEntity<Object> getCodeHtml() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html");
        return new ResponseEntity<>(code.getCode_html(), headers, HttpStatus.OK);
    }*/

    @GetMapping(path="/code/{N}", produces = "text/html")
    public ModelAndView getCodeHtml(@PathVariable("N") String N) {
        //HttpHeaders headers = new HttpHeaders();
        //headers.add("Content-Type", "text/html; charset=utf-8");
        //return new ResponseEntity<>(code.getCode_html(), headers, HttpStatus.OK);
        //response.addHeader("Content-Type", "text/html");
        //model.addObject("code", code.getCode());
        //System.out.println(map);
        //System.out.println(map.get("date"));
        var response = getApiCodeByID(N);
        if (response.getStatusCode()==HttpStatus.NOT_FOUND) {
            //System.out.println("OUT");
            ModelAndView model = new ModelAndView("error_template");
            model.setStatus(HttpStatus.NOT_FOUND);
            return model;
        } else {
            //System.out.println("normal");
            ModelAndView model = new ModelAndView("code_template");
            /*Map<String, Object> map = (Map<String, Object>) getApiCodeByID(N).getBody();
            model.addObject("code", map.get("code"));
            model.addObject("load_date_str", map.get("date"));
            model.addObject("time", map.get("time"));
            model.addObject("views", map.get("views"));*/

            Code map = (Code) response.getBody();
            model.addObject("code", map.getCode());
            model.addObject("load_date_str", map.getLoad_date_str());
            model.addObject("time", map.getTime());
            model.addObject("views", map.getViews());
            /*if (map.isViews_restricted()) {
                int remaining_views = map.getViews() - 1;
                map.setViews(remaining_views);
            }*/
            model.addObject("views_restricted", map.isViews_restricted());
            model.addObject("time_restricted", map.isTime_restricted());

            model.setStatus(HttpStatus.OK);

            return model;
        }
    }

    @GetMapping(path="/code/latest", produces = "text/html")
    public ModelAndView getCodeList() {
        List<Code> tail = (List<Code>) getApiCode().getBody();
        //response.addHeader("Content-Type", "text/html");
        //HttpHeaders headers = new HttpHeaders();
        //headers.add("Content-Type", "text/html");
        ModelAndView model = new ModelAndView("code_list");
        model.addObject("code_list", tail);
        model.setStatus(HttpStatus.OK);
        return model;
    }

    /*@GetMapping(path="/code/new")
    public ResponseEntity<Object> getCodeNew() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html");
        return new ResponseEntity<>(code.getNew_code_html(), headers, HttpStatus.OK);
    }*/

    @GetMapping(path="/code/new",produces = "text/html")
    public ModelAndView getCodeNew() {
        ModelAndView model = new ModelAndView("new_template");
        return model;
    }

    @GetMapping(path="/api/code/{N}")
    public ResponseEntity<Object> getApiCodeByID(@PathVariable("N") String N) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Code code = service.findById(N).orElse(null);
        //System.out.println(code);
        //var code = service.findById(N);
        //var code = service.getById(N);

        if (code != null) {
            if (code.isIs_secret()) {

                if (code.isTime_restricted()) {
                    long passed_time = code.computeRemainingTime(code.getLoad_date(), LocalDateTime.now());
                    code.setTime(code.getOriginal_time() - (int) passed_time);
                }

                if (code.isViews_restricted()) {
                    int remaining_views = code.getViews() - 1;
                    code.setViews(remaining_views);
                }

                if ((code.getTime() < 0 && code.isTime_restricted()) || (code.getViews() < 0) && code.isViews_restricted()) {
                    service.deleteById(N);
                    return new ResponseEntity<>(String.format("ID %s not found", N.toString()), headers, HttpStatus.NOT_FOUND);
                }

                /*if (code.isViews_restricted()) {
                    int remaining_views = code.getViews() - 1;
                    code.setViews(remaining_views);
                }*/

                service.save(code);
            }

            Map<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("code", code.getCode());
            //map.put("load date", code.getLoad_date());
            map.put("date", code.getLoad_date_str());
            //map.put("time", code.getTime());
            map.put("time", code.getOriginal_time());
            map.put("views", code.getViews());
            //map.put("views_restricted", code.isViews_restricted());
            //map.put("time_restricted", code.isTime_restricted());
            //map.put("secret", code.isIs_secret());

            return new ResponseEntity<>(code, headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(String.format("ID %s not found", N.toString()), headers, HttpStatus.NOT_FOUND);

    }

    @GetMapping(path="/api/code/latest")
    public ResponseEntity<Object> getApiCode() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Code> code_list = service.findAllNoSecrets();
        List<Code> tail = new ArrayList<>(code_list.subList(Math.max(code_list.size() - 10, 0), code_list.size()));
        Collections.reverse(tail);

        //map.put("code", code_list.get(N-1).getCode());
        //map.put("date", code_list.get(N-1).getLoad_date_str());

        return new ResponseEntity<>(tail, headers, HttpStatus.OK);

    }

    /*@PostMapping(path="/api/code/new")
    public ResponseEntity<Object> postApiCode(@RequestBody Code new_code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        code.setCode(new_code.getCode());
        code.setLoad_date(new_code.getLoad_date());
        code.setLoad_date_str();
        code.setCode_html();

        code_list.add(new_code);

        Map<String, String> map = new HashMap<>();
        int id = code_list.size();
        new_code.setId(id);
        map.put("id",Integer.toString(id));

        return new ResponseEntity<>(map, headers, HttpStatus.OK);

    }*/

    @PostMapping(path="/api/code/new")
    public ResponseEntity<Object> postApiCodeRepo(@RequestBody Code new_code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Map<String, UUID> map = new HashMap<>();
        UUID token = UUID.randomUUID();
        new_code.setId(token.toString());
        //long id = service.count()+1;
        //new_code.setId(token);
        map.put("id",token);

        if (new_code.getOriginal_time()>0 || new_code.getViews()>0) {
            new_code.setIs_secret(true);
            if (new_code.getOriginal_time()>0) {
                new_code.setTime_restricted(true);
            }
            if (new_code.getViews()>0 ) {
                new_code.setViews_restricted(true);
                //new_code.setViews(new_code.getViews()-1);
            }
        } else {
            new_code.setIs_secret(false);
        }

        service.save(new_code);

        return new ResponseEntity<>(map, headers, HttpStatus.OK);

    }


}
