package kondratkov.advocatesandnotariesrf.data_theme;

public class AllQuestions_forum {
    private int _id;
    private int idt;
    private int idu;
    private int idj;
    private String date;
    private String name;
    private int count;
    private int place;
    private int read;
    private String text;
    private int theme;

    // bПустой констуктор
    public AllQuestions_forum() {
    }

    // Конструктор с параметрами
    public AllQuestions_forum(int id, int idt, int idu, int idj, String date, String name, int count, int place, int read, String text, int theme) {

        this._id = id;
        this.idt = idt;
        this.idu = idu;
        this.idj = idj;
        this.date = date;
        this.name = name;
        this.count = count;
        this.place = place;
        this.read = read;
        this.text = text;
        this.theme = theme;
    }

    // Конструктор с параметрами
    public AllQuestions_forum(int idt, int idu, int idj, String date, String name, int count, int place, int read, String text, int theme){

        this.idt = idt;
        this.idu = idu;
        this.idj = idj;
        this.date = date;
        this.name = name;
        this.count = count;
        this.place = place;
        this.read = read;
        this.text = text;
        this.theme = theme;
    }

    // Создание геттеров-сеттеров
    public int getID() {return this._id;}
    public void setID(int id) {this._id = id;}

    public int get_idt() {return this.idt;}
    public void set_idt(int idt) {this.idt = idt;}

    public int get_idu() {return this.idu;}
    public void set_idu(int idu) {this.idu = idu;}

    public int get_idj() {return this.idj;}
    public void set_idj(int idj) {this.idj = idj;}

    public String get_date() {return this.date;}
    public void set_date(String date) {this.date = date;}

    public String get_name() {return this.name;}
    public void set_name(String name) {this.name = name;}

    public int get_count(){return this.count;}
    public void set_count(int count){this.count = count;}

    public int get_place(){return this.place;}
    public void set_place(int place){this.place = place;}

    public int get_read(){return this.read;}
    public void set_read(int read){this.read = read;}

    public String get_text() {return this.text;}
    public void set_text(String text) {this.text = text;}

    public int get_theme(){return this.theme;}
    public void set_theme(int theme){this.theme = theme;}
}
