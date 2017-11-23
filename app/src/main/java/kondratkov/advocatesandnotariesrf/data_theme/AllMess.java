package kondratkov.advocatesandnotariesrf.data_theme;

public class AllMess {
    private int _id;
    private int idt;
    private int idu;
    private int idj;
    private String date;
    private String name;
    private String text;
    private int place;
    private int tipwho;

    // b������ ����������
    public AllMess() {
    }

    // ����������� � �����������
    public AllMess(int id, int idt, int idu, int idj, String date, String name, String text, int place, int tipwho) {

        this._id = id;
        this.idt = idt;
        this.idu = idu;
        this.idj = idj;
        this.date = date;
        this.name = name;
        this.text = text;
        this.place = place;
        this.tipwho = tipwho;
    }

    // ����������� � �����������
    public AllMess(int idt, int idu, int idj, String date, String name, String text, int place, int tipwho){

        this.idt = idt;
        this.idu = idu;
        this.idj = idj;
        this.date = date;
        this.name = name;
        this.text = text;
        this.place = place;
        this.tipwho = tipwho;
    }

    // �������� ��������-��������
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

    public String get_text() {return this.text;}
    public void set_text(String text) {this.text = text;}

    public int get_place(){return this.place;}
    public void set_place(int place){this.place = place;}

    public int get_tipwho(){return this.tipwho;}
    public void set_tipwho(int tipwho){this.tipwho = tipwho;}
}
