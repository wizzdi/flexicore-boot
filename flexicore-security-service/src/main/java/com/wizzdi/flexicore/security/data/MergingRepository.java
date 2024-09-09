package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Basic;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.events.BasicCreated;
import com.wizzdi.flexicore.security.events.BasicUpdated;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


@Extension
@Component
public class MergingRepository implements Plugin {
    private static final Logger logger = LoggerFactory.getLogger(MergingRepository.class);
    @PersistenceContext
    private EntityManager em;


    @Transactional
    public <T> MergeResult<T> merge(T base, boolean updateDate,boolean propagateEvents) {
        Basic base1 = null;
        boolean created = false;
        if (base instanceof Basic) {
            OffsetDateTime now = OffsetDateTime.now();
            base1 = (Basic) base;
            created = base1.getUpdateDate() == null;
            if (updateDate) {
                base1.setUpdateDate(now);
            }
            if (created) {
                base1.setCreationDate(now);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("merging " + base1.getId() + " updateDate flag is " + updateDate + " update date " + base1.getUpdateDate());
            }


        }

        T merged = em.merge(base);
        Object event=null;
        if (propagateEvents) {
            if (base1 != null) {
                if (created) {
                    event=new BasicCreated<>(base1);
                } else {
                    event=new BasicUpdated<>(base1);

                }
            }
        }

        return new MergeResult<>(merged,event);

    }


    @Transactional
    public MassMergeResult massMerge(List<?> toMerge, boolean updatedate,boolean propagateEvents) {
        List<Object> events = new ArrayList<>();
        List<Object> merged=new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now();
        for (Object o : toMerge) {
            if (o instanceof Basic) {
                Basic baseclass = (Basic) o;
                boolean created = baseclass.getUpdateDate() == null;
                if (updatedate) {
                    baseclass.setUpdateDate(now);
                }
                if (created) {
                    baseclass.setCreationDate(now);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("merging " + baseclass.getId() + " updateDate flag is " + updatedate + " update date is " + baseclass.getUpdateDate());
                }
                if(!propagateEvents){
                    continue;
                }
                if (created) {
                    BasicCreated<?> baseclassCreated = new BasicCreated<>(baseclass);
                    events.add(baseclassCreated);
                } else {
                    BasicUpdated<?> baseclassUpdated = new BasicUpdated<>(baseclass);
                    events.add(baseclassUpdated);
                }

            }

            merged.add(em.merge(o));
        }

        return new MassMergeResult(merged,events);

    }
    public record MassMergeResult(List<?> merged, List<Object> events ){};

    public record MergeResult<T>(T merged, Object event ){};
}
