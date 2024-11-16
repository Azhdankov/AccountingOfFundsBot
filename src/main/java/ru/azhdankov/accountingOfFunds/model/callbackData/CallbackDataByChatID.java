package ru.azhdankov.accountingOfFunds.model.callbackData;

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
public class CallbackDataByChatID {
    @Id private String chatID;
    private String callbackData;
    private String callbackCategoryID;
}
