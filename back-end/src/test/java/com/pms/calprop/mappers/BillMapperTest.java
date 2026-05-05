package com.pms.calprop.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.pms.calprop.dto.BillRequest;
import com.pms.calprop.dto.BillResponse;
import com.pms.calprop.entities.Bill;

class BillMapperTest {

    private BillMapper billMapper;

    @BeforeEach
    void setUp() {
        billMapper = Mappers.getMapper(BillMapper.class);
    }

    @Test
    @DisplayName("Should convert Bill entity to BillResponse successfully")
    void shouldMapBillToBillResponse() {
        Bill bill = new Bill("Aluguel", new BigDecimal("1200.00"));
        bill.setId(10L);

        BillResponse response = billMapper.toResponse(bill);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(bill.getId());
        assertThat(response.description()).isEqualTo(bill.getDescription());
        assertThat(response.totalAmount()).isEqualByComparingTo(bill.getTotalAmount());
    }

    @Test
    @DisplayName("Should convert BillRequest to Bill entity and ignore the ID.")
    void shouldMapBillRequestToBill() {
        BillRequest request = new BillRequest("Internet", new BigDecimal("100.00"));

        Bill bill = billMapper.toEntity(request);

        assertThat(bill).isNotNull();
        assertThat(bill.getId()).isNull();
        assertThat(bill.getDescription()).isEqualTo(request.description());
        assertThat(bill.getTotalAmount()).isEqualByComparingTo(request.totalAmount());
    }

    @Test
    @DisplayName("Should update Bill entity from BillRequest preservando the ID.")
    void shouldUpdateBillFromRequest() {

        Long originalId = 55L;
        Bill existingBill = new Bill("Energia", new BigDecimal("150.00"));
        existingBill.setId(originalId);

        BillRequest updateRequest = new BillRequest("Energia", new BigDecimal("200.00"));

        billMapper.updateBillFromRequest(updateRequest, existingBill);
        assertThat(existingBill.getId()).isEqualTo(originalId);
        assertThat(existingBill.getDescription()).isEqualTo(updateRequest.description());
        assertThat(existingBill.getTotalAmount()).isEqualByComparingTo(updateRequest.totalAmount());
    }
}
