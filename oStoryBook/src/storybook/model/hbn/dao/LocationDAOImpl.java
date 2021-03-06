/*
Storybook: Open Source software for novelists and authors.
Copyright (C) 2008 - 2012 Martin Mustun, 2015 FaVdB

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package storybook.model.hbn.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import storybook.model.hbn.entity.AbstractEntity;
import storybook.model.hbn.entity.Location;
import storybook.model.hbn.entity.Person;
import storybook.toolkit.DateUtil;

public class LocationDAOImpl extends SbGenericDAOImpl<Location, Long> implements
		LocationDAO {

	public LocationDAOImpl() {
		super();
	}

	public LocationDAOImpl(Session session) {
		super(session);
	}

	public Location findTitle(String str) {
		String nstr=str.trim();
		List<Location> list = findAll();
		for (Location p:list) {
			if (p.getName().trim().equals(nstr)) return(p);
		}
		return(null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Location> findAll() {
		Query query = session.createQuery("from Location order by location_id,country,city,name");
		return (List<Location>) query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AbstractEntity> findAllByName() {
		Criteria crit = session.createCriteria(Location.class);
		crit.addOrder(Order.asc("name"));
		List<AbstractEntity> entities = (List<AbstractEntity>) crit.list();
		return entities;
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllInList() {
		Query query = session.createQuery("from Location order by location_id,country,city,name");
		List<String> list=new ArrayList<>();
		for (Location s : (List<Location>) query.list()) {
			list.add(s.getName());
		}
		return(list);
	}



	@SuppressWarnings("unchecked")
	public List<String> findCountries(){
		Query query = session.createQuery("select distinct(l.country) from Location as l order by l.country");
		return (List<String>)query.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> findCities(){
		Query query = session.createQuery("select distinct(l.city) from Location as l order by l.city");
		return (List<String>)query.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> findCitiesByCountry(String country) {
		if (country == null) {
			Query query = session.createQuery("select distinct(l.city) from Location as l where l.country is null order by l.city");
			return (List<String>) query.list();
		}
		Query query = session.createQuery("select distinct(l.city) from Location as l where l.country=:country order by l.city");
		query.setParameter("country", country);
		return (List<String>) query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Location> findByCountries(List<String> countries) {
		if (countries.isEmpty()) {
			return new ArrayList<Location>();
		}
		Query query = session.createQuery("from Location as l where l.country in (:countries)");
		query.setParameterList("countries", countries);
		return (List<Location>) query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Location> findByCountry(String country) {
		Criteria crit = session.createCriteria(Location.class);
		if (country == null) {
			crit.add(Restrictions.isNull("country"));
		} else {
			crit.add(Restrictions.eq("country", country));
		}
		List<Location> locations = (List<Location>) crit.list();
		return locations;
	}

	@SuppressWarnings("unchecked")
	public List<Location> findByCity(String city) {
		Criteria crit = session.createCriteria(Location.class);
		if (city == null) {
			crit.add(Restrictions.isNull("city"));
		} else {
			crit.add(Restrictions.eq("city", city));
		}
		List<Location> locations = (List<Location>) crit.list();
		return locations;
	}

	@SuppressWarnings("unchecked")
	public List<Location> findByCountryCity(String country, String city) {
		Criteria crit = session.createCriteria(Location.class);
		if (country == null) {
			crit.add(Restrictions.isNull("country"));
		} else {
			crit.add(Restrictions.eq("country", country));
		}
		if (city == null) {
			crit.add(Restrictions.isNull("city"));
		} else {
			crit.add(Restrictions.eq("city", city));
		}
		crit.addOrder(Order.asc("name"));
		List<Location> locations = (List<Location>) crit.list();
		return locations;
	}

	public long countByPersonLocationDate(Person person, Location location, Date date){
		date = DateUtil.getZeroTimeDate(date);
		Query query = session.createQuery(
				  "select count(s) from Scene as s" +
				  " join s.persons as p" +
				  " join s.locations as l" +
				  " where p=:person and l=:location"
				  +" and s.sceneTs between :tsStart and :tsEnd");
		query.setEntity("person", person);
		query.setEntity("location", location);
		Timestamp tsStart = new Timestamp(date.getTime());
		date = DateUtil.addDays(date, 1);
		date = DateUtil.addMilliseconds(date, -1);
		Timestamp tsEnd = new Timestamp(date.getTime());
		query.setTimestamp("tsStart", tsStart);
		query.setTimestamp("tsEnd", tsEnd);
		return (Long)query.uniqueResult();
	}
}
