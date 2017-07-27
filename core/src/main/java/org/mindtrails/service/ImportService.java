package org.mindtrails.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Diheng on 6/19/17.
 *
 * Import the data package from the outer server's export api and then insert it into the inner server's database.
 */

@Service
public class ImportService {

    private static final Logger LOG = LoggerFactory.getLogger(ImportService.class);

    @Autowired
    ExportService exportService;




}
