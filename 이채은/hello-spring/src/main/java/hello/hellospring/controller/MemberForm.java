package hello.hellospring.controller;

public class MemberForm {
    private String name;
    public String getName(){
        return name;
    }

    public void setName(String name) { //멤버스의 name 이랑 매칭되면서 form 이 들어옴.
        this.name=name;  // 그 이후, 실제 멤버 controller 에다가 create form 기능 넣어줘야함.
    }
}
