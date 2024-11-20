package ru.azhdankov.accountingOfFunds.model.groupCategoryInfo;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"categoryKey", "pairID"})})
public class GroupCategoryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String chatID;
    private String categoryKey;
    private String readableCategoryName;
    private Double amount;
    private String pairID;
}
