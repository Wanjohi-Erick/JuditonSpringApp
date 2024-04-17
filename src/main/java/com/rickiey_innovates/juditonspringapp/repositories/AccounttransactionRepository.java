package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Accounttransaction;
import com.rickiey_innovates.juditonspringapp.models.Bankaccount;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccounttransactionRepository extends JpaRepository<Accounttransaction, Integer> {
    @Transactional
    @Modifying
    @Query("delete from Accounttransaction a where a.ref = ?1 and a.farm = ?2")
    int deleteByRefAndFarm(String ref, Farm farm);
    @Query("select (count(a) > 0) from Accounttransaction a where a.ref = ?1 and a.farm = ?2")
    boolean existsByRefAndChurch(String ref, Farm farm);
    @Query("select a from Accounttransaction a where a.ref = ?1 and a.farm = ?2")
    Accounttransaction findByRefAndChurch(String ref, Farm farm);
    @Query("select a from Accounttransaction a where a.id = ?1 and a.ref = ?2")
    Accounttransaction findByIdAndRef(Integer id, String ref);
    @Query("select a from Accounttransaction a where a.bank = ?1 and a.description = ?2")
    Accounttransaction findByBankAndDescription(Bankaccount bank, String description);
    @Query("select a from Accounttransaction a where a.account = ?1 or a.account = ?2 order by a.id limit 8")
    List<Accounttransaction> findByAccountAndAccountOrderByIdAsc(int account, int account1);
    @Query("select a from Accounttransaction a where a.account <> ?1 and a.credit <> ?1")
    List<Accounttransaction> findByAccountAndCreditNot(Integer account);
    @Query("select a from Accounttransaction a where a.bank.id = ?1")
    List<Accounttransaction> findByBank_Id(Integer id);

    @Query("select p from Accounttransaction p where p.payeePayer not like ?1 and p.farm = ?2 order by p.id DESC LIMIT 7")
    List<Accounttransaction> findByPayeeNameNotLikeAndChurchLikeOrderByIdDesc(String s, Farm farm);

    @Query("select a from Accounttransaction a where a.farm = ?1")
    List<Accounttransaction> findByFarm(Farm farm);
}