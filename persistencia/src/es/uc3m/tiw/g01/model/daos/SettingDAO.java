package es.uc3m.tiw.g01.model.daos;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import es.uc3m.tiw.g01.model.entities.Setting;

public interface SettingDAO {
  public abstract Setting setUpSettings(String email, String password, int stockThresHoldLow,
      int stockThresHoldVeryLow) throws NotSupportedException, SystemException, SecurityException,
      IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException;

  public abstract Setting getSetting();
}
