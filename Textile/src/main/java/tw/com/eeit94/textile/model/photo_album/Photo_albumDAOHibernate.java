/*
 * 假設"Table名稱"為"example"，套件名稱用tw.com.eeit94.textile.model."Table名稱"。
 */
package tw.com.eeit94.textile.model.photo_album;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/*
 * Hibernate DAO產生步驟：
 * 1. Hibernate DAO名稱為'"Table名稱" + "DAOHibernate"'。
 * 2. 實作'"Table名稱" + "DAO"'介面，並覆寫方法。
 * 3. 標記@Repository(value = '"Table名稱(第一個字母小寫)" + "DAO"')。
 * 4. 加入Bean元件並標記@Autowired。
 */
@Repository(value = "photo_albumDAO")
public class Photo_albumDAOHibernate implements Photo_albumDAO {
	@Autowired
	private SessionFactory sessionFacotry;

	public Photo_albumDAOHibernate(SessionFactory sessionFacotry) {
		this.sessionFacotry = sessionFacotry;
	}

	private Session getSession() {
		return sessionFacotry.getCurrentSession();
	}

	// 測試程式
	public static void main(String args[]) {

	}

	@Override
	public List<Photo_albumBean> select() {
		return this.getSession().createQuery("FROM Photo_albumBean", Photo_albumBean.class).getResultList();
	}

	@Override
	public Photo_albumBean selectByAlbumNo(Photo_albumBean bean) {
		return getSession().get(Photo_albumBean.class, bean.getAlbumno());
	}

	@Override
	public List<Photo_albumBean> selectByOthers(Photo_albumBean bean) {
		CriteriaBuilder cb = getSession().getCriteriaBuilder();
		CriteriaQuery<Photo_albumBean> query = cb.createQuery(Photo_albumBean.class);
		Root<Photo_albumBean> root = query.from(Photo_albumBean.class);
//		List<Predicate> coll = new ArrayList<Predicate>();
//		if(bean.getAlbumname() != null){
//			coll.add(cb.like(root.<String>get("albumname"),	"%" + bean.getAlbumname() + "%"));
//		}
//		if(bean.getIntroduction() != null){
//			coll.add(cb.like(root.<String>get("albumname"),	"%" + bean.getAlbumname() + "%"));
//		}
//		if(bean.getVisibility() != null){
//			coll.add(cb.like(root.<String>get("visibility"),"%" + bean.getVisibility() + "%"));
//		}
//		if(bean.getmId() != null){
//			coll.add(cb.equal(root.<Integer>get("mId"), bean.getmId()));
//		}		
		
		Predicate p1 = cb.like(root.<String>get("albumname"),
				bean.getAlbumname() == null ? "%" : "%" + bean.getAlbumname() + "%");
		Predicate p2 = cb.like(root.<String>get("introduction"),
				bean.getIntroduction() == null ? "%" : "%" + bean.getIntroduction() + "%");
		Predicate p3 = cb.like(root.<String>get("visibility"),
				bean.getVisibility() == null ? "%" : "%" + bean.getVisibility() + "%");
		Predicate p4;
		if (bean.getmId() != null) {
			p4 = cb.equal(root.<Integer>get("mId"), bean.getmId());
		} else {
			p4 = cb.ge(root.<Integer>get("mId"), 0);
		}
		return getSession().createQuery(query.where(p1,p2,p3,p4)).getResultList();
	}

	@Override
	public Photo_albumBean insert(Photo_albumBean bean) {
		if (bean.getAlbumno() == null) {
			this.getSession().save(bean);
			return bean;
		}
		return null;
	}

	@Override
	public Photo_albumBean update(Photo_albumBean bean) {
		Photo_albumBean select = this.selectByAlbumNo(bean);
		if (select != null) {
			select.setAlbumname(bean.getAlbumname());
			select.setIntroduction(bean.getIntroduction());
			select.setVisibility(bean.getVisibility());
			select.setmId(bean.getmId());
			return select;
		}
		return null;
	}

	@Override
	public boolean delete(Photo_albumBean bean) {
		if (this.selectByAlbumNo(bean) != null) {
			getSession().delete(this.selectByAlbumNo(bean));
			return true;
		}
		return false;
	}

}