package ru.azhdankov.accountingOfFunds.model.categoryInfo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"chatID", "categoryKey"})})
public class CategoryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String chatID;
    private String categoryKey;
    private String readableCategoryName;
    private Double amount;
}
