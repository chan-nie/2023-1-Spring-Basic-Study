package hello.hellospring.controller;

public class MemberForm {
    private String name; //createMemberForm.html의 name과 매칭되며 데이터가 들어옴
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
