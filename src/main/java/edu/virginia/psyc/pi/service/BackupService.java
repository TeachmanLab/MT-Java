package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.persistence.Questionnaire.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Automated process that extracts sensitive data from the database, writes that data out to
 * a text file, and sends the data to a remote server over ssh.    Once data is successfully
 * transfered and verified it is removed locally.
 *
 * It determines which repositories to do this to, based on an annotation applied to the
 * repository.
 */
@Service
public class BackupService {

    @Autowired
    private static final Logger LOG = LoggerFactory.getLogger(BackupService.class);

    @Autowired private DASS21_ASRepository dass21_asRepository;
    @Autowired private DASS21_DSRepository dass21_dsRepository;
    @Autowired private QOLRepository qol_Repository;
    @Autowired private AUDIT_Repository audit_Repository;
    @Autowired private FollowUp_ChangeInTreatment_Repository followup_Repository;
    @Autowired private ImpactAnxiousImagery_Repository impact_Repository;
    @Autowired private MentalHealthHxTxRepository mh_Repository;
    @Autowired private MultiUserExperienceRepository mue_Repository;
    @Autowired private PilotUserExperienceRepository pue_Repository;
    @Autowired private CredibilityRepository credibilityRepository;
    @Autowired private DemographicRepository demographicRepository;
    @Autowired private ImageryPrimeRepository imageryPrimeRepository;
    @Autowired private StateAnxietyRepository stateAnxiety_Repository;
    @Autowired private RR_Repository rr_repository;
    @Autowired private CCRepository cc_repository;
    @Autowired private ReRuRepository reru_repository;
    @Autowired private DDRepository dd_repository;
    @Autowired private DD_FURepository dd_fu_repository;
    @Autowired private StateAnxietyPostRepository stateAnxietyPost_Repository;
    @Autowired private BBSIQRepository bbsiqRepository;
    @Autowired private AnxietyTriggersRepository anxietyTriggersRepository;
    @Autowired private SUDSRepository sudsRepository;
    @Autowired private VividRepository vividRepository;
    @Autowired private ReasonsForEndingRepository reasonsForEndingRepository;
    @Autowired private CIHSRepository cihsRepository;






}
