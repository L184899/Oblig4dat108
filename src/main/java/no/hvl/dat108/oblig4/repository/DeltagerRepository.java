package no.hvl.dat108.oblig4.repository;

import no.hvl.dat108.oblig4.model.Deltager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeltagerRepository extends JpaRepository<Deltager, String> {

    List<Deltager> findAllByOrderByFornavnAscEtternavnAsc();
}