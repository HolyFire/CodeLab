/**
 * Created by DELL on 14-7-10.
 */
public class TestEquals {
    public static void main(String[] args) {
        Person p1=new Person(5,"aaa");
        Person p2=new Person(5,"aaa");
        System.out.println(p1.equals(p2));

    }



}

class Person{
    int id;
    String name;
    public Person(int id,String name){
        this.id=id;
        this.name=name;
    }
@Override
    public boolean equals(Object obj){
    Person p=(Person)obj;
    return this.name==p.name;
}

}
