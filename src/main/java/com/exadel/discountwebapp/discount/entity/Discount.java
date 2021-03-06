package com.exadel.discountwebapp.discount.entity;

import com.exadel.discountwebapp.category.entity.Category;
import com.exadel.discountwebapp.location.entity.Location;
import com.exadel.discountwebapp.tag.entity.Tag;
import com.exadel.discountwebapp.user.entity.User;
import com.exadel.discountwebapp.userdiscount.entity.UserDiscount;
import com.exadel.discountwebapp.vendor.entity.Vendor;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discount")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    @Column(name = "dis_id")
    private Long id;

    @Column(name = "dis_title", length = 100, nullable = false)
    private String title;

    @EqualsAndHashCode.Exclude
    @Column(name = "dis_short_description", length = 255, nullable = false)
    private String shortDescription;

    @EqualsAndHashCode.Exclude
    @Column(name = "dis_description", length = 510, nullable = false)
    private String description;

    @EqualsAndHashCode.Exclude
    @Column(name = "dis_image_url", length = 510)
    private String imageUrl;

    @Column(name = "dis_percentage")
    private BigDecimal percentage;

    @Column(name = "dis_flat_amount")
    private BigDecimal flatAmount;

    @EqualsAndHashCode.Exclude
    @Column(name = "dis_start_date", nullable = false)
    private LocalDateTime startDate;

    @EqualsAndHashCode.Exclude
    @Column(name = "dis_expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "dis_promocode", length = 100)
    private String promocode;

    @Column(name = "dis_viewed")
    private Long viewNumber;

    @Version
    @Column(name = "dis_version", nullable = false)
    private Long version;

    @EqualsAndHashCode.Exclude
    @CreatedDate
    @Column(name = "dis_created", nullable = false, updatable = false)
    private LocalDateTime created;

    @EqualsAndHashCode.Exclude
    @LastModifiedDate
    @Column(name = "dis_modified", nullable = false)
    private LocalDateTime modified;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "vn_id", nullable = false)
    private Vendor vendor;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "cat_id", nullable = false)
    private Category category;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "location_discount",
            joinColumns = @JoinColumn(name = "dis_id"),
            inverseJoinColumns = @JoinColumn(name = "loc_id")
    )
    private List<Location> locations = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "tag_discount",
            joinColumns = @JoinColumn(name = "dis_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "discount")
    @EqualsAndHashCode.Exclude
    private List<UserDiscount> users = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "dis_id"),
            inverseJoinColumns = @JoinColumn(name = "usr_id")
    )
    private List<User> userFavorites = new ArrayList<>();
}
