// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.repository;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.repository.ArchiveItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect ArchiveItemRepository_Roo_Jpa_Repository {
    
    declare parents: ArchiveItemRepository extends JpaRepository<ArchiveItem, Long>;
    
    declare parents: ArchiveItemRepository extends JpaSpecificationExecutor<ArchiveItem>;
    
    declare @type: ArchiveItemRepository: @Repository;
    
}