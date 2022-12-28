package platform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
@Entity
@Table
@JsonPropertyOrder({ "code", "date", "time", "views"})
public class Code {

    @Id
    @JsonIgnore
    private String id;
    @Transient
    @JsonIgnore
    private static final String DATE_FORMATTER= "yyyy/MM/dd HH:mm:ss";
    @Column
    private String code;
    @Transient
    @JsonIgnore
    private String code_html;
    @Transient
    @JsonIgnore
    private String new_code_html;
    //@Transient
    @JsonIgnore
    @Column
    private LocalDateTime load_date;
    @Column
    @JsonProperty(value="date")
    private String load_date_str;

    //@JsonIgnore
    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int original_time;

    @Column
    private int views;

    @Transient
    @JsonIgnore
    private String template = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Code</title>
                </head>
                <body>
                <div>
                <span id="load_date" style="color: green">%s</span>
                <pre id="code_snippet" style="padding: 10px; border-style: solid; background-color: lightgray; border-width: 2px;max-width: 600px">%s</pre>
                </div>
                </body>
                </html>
                """;
    @Transient
    @JsonIgnore
    private String new_template = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Create</title>
                </head>
                <body>
                <script>
                    function send() {
                    let object = {
                        "code": document.getElementById("code_snippet").value,
                        "time": document.getElementById("time_restriction").value,
                        "views": document.getElementById("views_restriction").value            
                    };
                    
                                
                    let json = JSON.stringify(object);
                                
                    let xhr = new XMLHttpRequest();
                    xhr.open("POST", '/api/code/new', false)
                    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
                    xhr.send(json);
                    
                    console.log(json);
                                
                    if (xhr.status == 200) {
                      alert("Success!");
                    }
                }
                </script>
                <div>
            
                <textarea id="code_snippet" style="padding: 5px;width: 600px; height: 100px">// write your code here</textarea>
                
                <br>
                <p>Time restriction: </p><input id="time_restriction" type="text" value="0" >
                <br>
                
                <p>Views restriction: </p><input id="views_restriction" type="text" value="0" >
                <br>
                <br>
                <button id="send_snippet" type="submit" onclick="send()">Submit</button>
                </div>
                </body>
                </html>
                """;
    @JsonIgnore
    @Column
    private boolean is_secret;

    @Column
    //@JsonProperty(value = "time")
    //@JsonIgnore
    private int time;
    @JsonIgnore
    @Column
    private boolean time_restricted;
    @JsonIgnore
    @Column
    private boolean views_restricted;

    public Code(String code, int time, int views) {
        System.out.println("non-empty constructor call --------------");
        this.code = code;
        this.time = time;
        this.views = views;
    }

    public Code() {
        System.out.println("empty constructor call --------------");
        this.code ="public static void main(String[] args) {\n    SpringApplication.run(CodeSharingPlatform.class, args);\n}";
        this.load_date = LocalDateTime.now();
        setLoad_date_str();
        this.code_html = String.format(template, this.getLoad_date_str(), this.getCode());
        this.new_code_html = new_template;
    }

    public boolean isTime_restricted() {
        return time_restricted;
    }

    public void setTime_restricted(boolean time_restricted) {
        this.time_restricted = time_restricted;
    }

    public boolean isViews_restricted() {
        return views_restricted;
    }

    public void setViews_restricted(boolean views_restricted) {
        this.views_restricted = views_restricted;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode_html() {
        return code_html;
    }

    public void setCode_html() {
        this.code_html = String.format(template, this.getLoad_date_str(), this.getCode());;
    }

    public LocalDateTime getLoad_date() {
        return load_date;
    }

    public void setLoad_date(LocalDateTime load_date) {
        this.load_date = load_date;
    }

    public String getLoad_date_str() {
        return load_date_str;
    }

    public void setLoad_date_str() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        this.load_date_str = this.load_date.format(formatter);
    }

    public String getNew_code_html() {
        return new_code_html;
    }

    public void setNew_code_html(String new_code_html) {
        this.new_code_html = new_code_html;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    /*public void computeRemainingViews() {
        this.views = this.views-1;
    }*/

    public long computeRemainingTime(LocalDateTime start, LocalDateTime end) {
        long time = ChronoUnit.SECONDS.between(start, end);
        //Duration duration = Duration.between(this.getLoad_date(), LocalDateTime.now());
        //long time = duration.getSeconds();
        System.out.println("-----------------");
        System.out.println(start);
        System.out.println(end);
        System.out.println(time);
        return time;
    }

    public boolean isIs_secret() {
        return is_secret;
    }

    public void setIs_secret(boolean is_secret) {
        this.is_secret = is_secret;
    }

    //@JsonIgnore
    public int getOriginal_time() {
        return original_time;
    }

    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setOriginal_time(int original_time) {
        this.original_time = original_time;
    }
}
