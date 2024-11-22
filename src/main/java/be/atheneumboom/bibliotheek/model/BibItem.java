package be.atheneumboom.bibliotheek.model;

import jakarta.persistence.*;
import lombok.Data;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Random;

@Data
@MappedSuperclass
public class BibItem {
    private String type;
    private String titel;
    private String code = "testcode";
    private Boolean uitgeleend;
    @ManyToOne
    @JoinColumn(name = "id_lener")
    private User geleendDoor;
    private LocalDate uitgeleendTot;
    @ManyToOne
    @JoinColumn(name = "id_reserv")
    private User gereserveerdDoor;
    private LocalDate gereserveerdTot;

    public BibItem(String titel) {
        this.titel = titel;
        this.code = generateCode();
    }
    public BibItem() {
    }

    private String generateCode(){
        StringBuilder code = new StringBuilder();
        code.append(this.getTitel().substring(0,3))
                .append(new Random().nextInt(1000));
        return code.toString();
    }

    public Long getItemId() throws InvocationTargetException, IllegalAccessException {
        Long id = null;
        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {
            if (declaredMethod.getName().contains("id")){
                id = Long.getLong(declaredMethod.invoke(this).toString());
            }
        }
        return id;
    }

    @Override
    public String toString() {
        return "titel='" + titel;
    }
}
