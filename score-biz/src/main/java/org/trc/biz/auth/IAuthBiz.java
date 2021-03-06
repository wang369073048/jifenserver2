package org.trc.biz.auth;

import com.txframework.core.jdbc.PageRequest;
import org.trc.domain.auth.Auth;
import org.trc.domain.dto.AuthQueryDTO;
import org.trc.domain.pagehome.Banner;
import org.trc.domain.pagehome.BannerContent;
import org.trc.form.auth.AuthForm;
import org.trc.form.pagehome.BannerContentForm;
import org.trc.util.Pagenation;

/**
 * Created by hzwzhen on 2017/6/14.
 */
public interface IAuthBiz {

    /**
     * 保存
     * @param auth
     * @return
     * @throws Exception
     */
    int saveAuth(Auth auth);

    /**
     * 更新
     * @param auth
     * @return
     * @throws Exception
     */
    int updateAuth(Auth auth);

    Auth getNativeAuthByUserId(String userId);

    Auth getAuthByUserId(String userId);

    Auth getAuthById(Long id);

    Auth getAuthByShopId(Long shopId);

    /**
     * Auth分页查询
     * @return
     * @throws Exception
     */
    Pagenation<Auth> queryAuthListForPage(AuthQueryDTO query, Pagenation<Auth> pageRequest);

}
