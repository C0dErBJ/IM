package com.AlphaZ.dao;

import com.AlphaZ.constant.StatusCode;
import com.AlphaZ.entity.AlphazUserEntity;
import com.AlphaZ.entity.PageDTO;
import com.AlphaZ.util.valid.ValideHelper;
import com.AlphaZ.viewmodel.OperationModel;
import com.AlphaZ.viewmodel.UserRoleModel;
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
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager em;

    public Map<String, List<String>> getRoleByUserID(long userid) {
        String sql = "SELECT DISTINCT e.id mid ,f.id oid ,e.menunameen menuname, f.operationnameen operationname FROM alphaz_user AS a " +
                "LEFT JOIN alphaz_user_role AS b ON a.id = b.userid " +
                "LEFT JOIN alphaz_role_menu_operation AS c ON b.roleid = c.roleid " +
                "LEFT JOIN alphaz_menu_operation AS d ON c.menuid = d.id " +
                "RIGHT JOIN alphaz_menu AS e ON d.menuid = e.id " +
                "LEFT JOIN alphaz_operation AS f ON f.id = d.operationid " +
                "RIGHT JOIN alphaz_role AS g ON g.id = b.roleid WHERE a.id = ? " +
                " AND f.`status` = " + StatusCode.SUCCESS +
                " AND a.`status`=" + StatusCode.SUCCESS +
                " AND b.`status`=" + StatusCode.SUCCESS +
                " AND c.`status`=" + StatusCode.SUCCESS +
                " AND d.`status`=" + StatusCode.SUCCESS +
                " AND e.`status`=" + StatusCode.SUCCESS + " ORDER BY e.id";
        Map<String, List<String>> data1 = new HashMap<String, List<String>>();
        List<UserRoleModel> data = (List<UserRoleModel>) this.jdbcTemplate.query(sql, ps -> {
                    ps.setLong(1, userid);
                }, new ResultSetExtractor() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException,
                            DataAccessException {
                        List<UserRoleModel> data = new ArrayList<>();
                        UserRoleModel ur = new UserRoleModel();
                        List<OperationModel> oml = new ArrayList<OperationModel>();
                        Long menuid = null;
                        String menuname = null;
                        boolean isContained = false;
                        while (rs.next()) {
                            Long mid = rs.getLong("mid");
                            Long oid = rs.getLong("oid");
                            String mname = rs.getString("menuname");
                            String operationname = rs.getString("operationname");
                            if (menuid == mid || menuid == null) {
                                menuid = mid;
                                menuname = mname;
                                OperationModel om = new OperationModel();
                                om.id = oid;
                                om.operationName = operationname;
                                oml.add(om);
                            } else {
                                ur.menuid = menuid;
                                ur.menuName = menuname;
                                ur.operation = oml;
                                UserRoleModel ur1 = new UserRoleModel(ur);
                                oml = new ArrayList<OperationModel>();
                                data.add(ur1);
                                OperationModel om = new OperationModel();
                                om.id = oid;
                                om.operationName = operationname;
                                oml.add(om);
                                menuid = mid;
                                menuname = mname;
                            }
                            if (rs.isLast()) {
                                ur.menuid = menuid;
                                ur.menuName = menuname;
                                ur.operation = oml;
                                UserRoleModel ur1 = new UserRoleModel(ur);
                                oml = new ArrayList<OperationModel>();
                                data.add(ur1);
                            }
                        }

                        return data;
                    }
                }
        );
        List<String> mapoml = new ArrayList<String>();
        Map<String, List<String>> mapdata = new HashMap<String, List<String>>();
        for (UserRoleModel userRoleModel : data) {
            List<OperationModel> op = userRoleModel.getOperation();
            for (OperationModel operationModel : op) {
                mapoml.add(operationModel.operationName);
            }
            List<String> mapoml1 = new ArrayList<String>(mapoml);
            if (userRoleModel.getMenuName() != null) {
                mapdata.put(userRoleModel.getMenuName(), mapoml1);
            }

            mapoml = new ArrayList<String>();
        }
        return mapdata;
    }

    public Map<String, List<String>> getMenuKVByUserID(long userid) {
        String sql = "SELECT  e.id mid ,f.id oid ,e.url, f.operationnameen operationname FROM alphaz_user AS a " +
                "LEFT JOIN alphaz_user_role AS b ON a.id = b.userid " +
                "LEFT JOIN alphaz_role_menu_operation AS c ON b.roleid = c.roleid " +
                "LEFT JOIN alphaz_menu_operation AS d ON c.menuid = d.id " +
                "RIGHT JOIN alphaz_menu AS e ON d.menuid = e.id " +
                "LEFT JOIN alphaz_operation AS f ON f.id = d.operationid " +
                "RIGHT JOIN alphaz_role AS g ON g.id = b.roleid WHERE a.id = ? " +
                " AND f.`status` = " + StatusCode.SUCCESS +
                " AND a.`status`=" + StatusCode.SUCCESS +
                " AND b.`status`=" + StatusCode.SUCCESS +
                " AND c.`status`=" + StatusCode.SUCCESS +
                " AND d.`status`=" + StatusCode.SUCCESS +
                " AND e.url != ''" +
                " AND e.`status`=" + StatusCode.SUCCESS +
                " ORDER BY e.id,f.`order` ";
        List<UserRoleModel> data = (List<UserRoleModel>) jdbcTemplate.query(sql, ps -> {
                    ps.setLong(1, userid);
                }, (ResultSetExtractor) rs -> {
                    List<UserRoleModel> data2 = new ArrayList<>();
                    UserRoleModel ur = new UserRoleModel();
                    List<OperationModel> oml = new ArrayList<OperationModel>();
                    Long menuid = null;
                    String menuname = null;
                    while (rs.next()) {
                        Long mid = rs.getLong("mid");
                        Long oid = rs.getLong("oid");
                        String mname = rs.getString("url");
                        String operationname = rs.getString("operationname");
                        if (menuid == mid || menuid == null) {
                            menuid = mid;
                            menuname = mname;
                            OperationModel om = new OperationModel();
                            om.id = oid;
                            om.operationName = operationname;
                            oml.add(om);
                        } else {
                            ur.menuid = menuid;
                            ur.menuName = menuname;
                            ur.operation = oml;
                            UserRoleModel ur1 = new UserRoleModel(ur);
                            oml = new ArrayList<OperationModel>();
                            data2.add(ur1);
                            OperationModel om = new OperationModel();
                            om.id = oid;
                            om.operationName = operationname;
                            oml.add(om);
                            menuid = mid;
                            menuname = mname;
                        }
                        if (rs.isLast()) {
                            ur.menuid = menuid;
                            ur.menuName = menuname;
                            ur.operation = oml;
                            UserRoleModel ur1 = new UserRoleModel(ur);
                            oml = new ArrayList<OperationModel>();
                            data2.add(ur1);
                        }
                    }
                    return data2;
                }
        );
        List<String> mapoml = new ArrayList<String>();
        Map<String, List<String>> mapdata = new HashMap<String, List<String>>();
        for (UserRoleModel userRoleModel : data) {
            List<OperationModel> op = userRoleModel.getOperation();
            for (OperationModel operationModel : op) {
                mapoml.add(operationModel.operationName);
            }
            List<String> mapoml1 = new ArrayList<String>(mapoml);
            if (userRoleModel.getMenuName() != null) {
                mapdata.put(userRoleModel.getMenuName(), mapoml1);
            }
            mapoml = new ArrayList<String>();
        }
        return mapdata;
    }

    public PageDTO getUserListByParams(int pagesize, int pageno, String username) {
        String userparams = null;
        int rowcount = 0;
        int start = (pageno - 1) * pagesize;
        Query q;
        if (!ValideHelper.isNullOrEmpty(username)) {
            userparams = "%" + username + "%";
        }
        String sql = null;
        if (userparams == null) {
            sql = "select count(*)  as countuser from alphaz_user where `status`=0";
        } else {
            sql = "select count(*)  as countuser from alphaz_user where `status`=0 and name like :name";
        }


        q = this.em.createNativeQuery(sql);
        if (!ValideHelper.isNullOrEmpty(username)) {
            q.setParameter("name", userparams);

        }

        rowcount = ((BigInteger) q.getSingleResult()).intValue();

        if (userparams == null) {
            sql = "select * from alphaz_user where `status`=0 ";

            sql += "order by createtime desc LIMIT " + start + "," + pagesize + ";";
        } else {

            sql = "select * from alphaz_user where `status`=0 and name like :name  ";

            sql += "order by createtime desc LIMIT " + start + "," + pagesize + ";";
        }

        q = this.em.createNativeQuery(sql, AlphazUserEntity.class);
        if (!ValideHelper.isNullOrEmpty(username)) {
            q.setParameter("name", userparams);
        }

        List<AlphazUserEntity> i = q.getResultList();
        PageDTO result = new PageDTO();
        result.setPageNo(pageno);
        result.setPageSize(pagesize);
        result.setRowCount(rowcount);
        result.setResultList(i);
        return result;
    }


    public UserViewModel login(String username, String password) {
        UserViewModel uvm = new UserViewModel();
        String sql = "SELECT  a.name,  a.avatar,  d.rolename, d.id ,a.id userid " +
                "FROM alphaz_user a  JOIN alphaz_user_role c ON a.id = c.userid  " +
                "JOIN alphaz_role d ON d.id = c.roleid " +
                "WHERE a.status = " + StatusCode.SUCCESS +
                " AND c.status = " + StatusCode.SUCCESS +
                " AND d.status = " + StatusCode.SUCCESS +
                " AND a.name=? and a.password=?";

        jdbcTemplate.query(sql, ps -> {
            ps.setString(1, username);
            ps.setString(2, password);
        }, rs -> {
            uvm.name = rs.getString("name");
            uvm.userid = rs.getLong("userid");
            uvm.avatar = rs.getLong("avatar");
            if (uvm.role == null) {
                uvm.role = new HashMap<>();
                uvm.role.put(rs.getInt("id"), rs.getString("rolename"));
            } else {
                uvm.role.put(rs.getInt("id"), rs.getString("rolename"));
            }
        });
        return uvm.userid != null ? uvm : null;
    }

    public boolean isSystemRole(Long userId) {
        Integer[] isS = new Integer[1];
        String sql = " SELECT c.issystem\n" +
                "FROM alphaz_user AS a\n" +
                "  JOIN alphaz_user_role AS b ON a.id = b.userid\n" +
                "  JOIN alphaz_role AS c ON b.roleid = c.id\n" +
                "WHERE a.status = 0 AND b.status = 0 AND c.status = 0 AND a.id = ?";
        jdbcTemplate.query(sql, ps -> {
            ps.setLong(1, userId);
        }, rs -> {
            isS[0] = rs.getInt("issystem");
        });
        return isS[0] == 0;
    }


}
