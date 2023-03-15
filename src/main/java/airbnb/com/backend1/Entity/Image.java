package airbnb.com.backend1.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Image")
@Table(name = "image")
@Getter
@Setter
public class Image {
    @Id
    @SequenceGenerator(
        name = "image_sequence",
        allocationSize = 1,
        sequenceName = "image_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "image_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;


    @NotBlank(message = "filename must be not blank")
    @Column(name = "file_name", nullable = false, unique = true)
    private String fileName;

    @NotBlank(message = "type must be not blank")
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "databyte", nullable = false)
    private byte[] databyte;

    public Image( String fileName, String type, byte[] databyte) {
        this.fileName = fileName;
        this.type = type;
        this.databyte = databyte;
    }

    

}
