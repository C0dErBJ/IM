package com.AlphaZ.dao;

import com.AlphaZ.constant.StatusCode;
import com.AlphaZ.entity.AlphazUserEntity;
import com.AlphaZ.entity.PageDTO;
import com.AlphaZ.util.valid.ValideHelper;
import com.AlphaZ.viewmodel.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by admin on 2017/1/13.
 */


@Repository
public class UserDAO extends BaseDAO<AlphazUserEntity, Long> {

    public UserViewModel login(String username, String password) {
        UserViewModel uvm = new UserViewModel();
        String sql = "SELECT  a.name,  a.avatar ,a.id userid " +
                "FROM alphaz_user a  " +
                "WHERE a.status = " + StatusCode.SUCCESS +
                " AND a.name=? and a.password=?";

        this.getJdbcTemplate().query(sql, ps -> {
            ps.setString(1, username);
            ps.setString(2, password);
        }, rs -> {
            uvm.username = rs.getString("name");
            uvm.userid = rs.getLong("userid");
            uvm.avatar = rs.getLong("avatar");
            uvm.state="在线";
        });
        return uvm.userid != null ? uvm : null;
    }


}
