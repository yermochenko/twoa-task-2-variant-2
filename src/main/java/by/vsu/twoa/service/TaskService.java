package by.vsu.twoa.service;

import by.vsu.twoa.dao.DaoException;
import by.vsu.twoa.dao.TaskDao;
import by.vsu.twoa.dao.UserDao;
import by.vsu.twoa.domain.Task;
import by.vsu.twoa.domain.User;
import by.vsu.twoa.geometry.*;
import by.vsu.twoa.geometry.relpos.PointTriangleRelativePosition;
import by.vsu.twoa.service.exception.ServiceException;
import by.vsu.twoa.service.exception.TaskNotExistsException;
import by.vsu.twoa.service.exception.UserNotExistsException;

import java.util.Date;
import java.util.List;

import static java.lang.Math.abs;

public class TaskService {
	private TaskDao taskDao;
	private UserDao userDao;

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public List<Task> findByOwner(Integer id) throws ServiceException {
		try {
			User owner = userDao.read(id).orElseThrow(() -> new UserNotExistsException(id));
			List<Task> tasks = taskDao.readByOwner(id);
			tasks.forEach(task -> task.setOwner(owner));
			return tasks;
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}


	public Task findById(Integer id) throws ServiceException {
		try {
			Task task = taskDao.read(id).orElseThrow(() -> new TaskNotExistsException(id));
			task.setOwner(userDao.read(task.getOwner().getId()).orElseThrow(() -> new UserNotExistsException(id)));
			Triangle triangle = task.getTriangle();
			Point a = triangle.getVertex1();
			Point b = triangle.getVertex2();
			Point c = triangle.getVertex3();
			Point p = task.getPoint();
			if(equal(p, a) || equal(p, b) || equal(p, c)) {
				task.setPointTriangleRelativePosition(PointTriangleRelativePosition.VERTEX);
			} else {
				Line ab = new Line(a, b);
				Line bc = new Line(b, c);
				Line ac = new Line(a, c);
				double pAB = lineEq(ab, p);
				double pBC = lineEq(bc, p);
				double pAC = lineEq(ac, p);
				if(abs(pAB) < 0.0001 && abs(pBC) < 0.0001 && abs(pAC) < 0.0001) {
					task.setPointTriangleRelativePosition(PointTriangleRelativePosition.ONSIDE);
				} else {
					double cAB = lineEq(ab, c);
					double aBC = lineEq(bc, a);
					double bAC = lineEq(ac, b);
					if(pAB * cAB < 0 && pBC * aBC < 0 && pAC * bAC < 0) {
						task.setPointTriangleRelativePosition(PointTriangleRelativePosition.INSIDE);
					} else {
						task.setPointTriangleRelativePosition(PointTriangleRelativePosition.OUTSIDE);
					}
				}
			}
			return task;
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Integer save(Task task) throws ServiceException {
		try {
			if(task.getId() == null) {
				task.setCreated(new Date(0));
				return taskDao.create(task);
			} else {
				throw new RuntimeException("Update operation not implemented yet");
			}
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}

	private static boolean equal(Point a, Point b) {
		return abs(a.getX() - b.getX()) < 0.0001 && abs(a.getY() - b.getY()) < 0.0001;
	}

	private static double lineEq(Line line, Point point) {
		return line.getA() * point.getX() + line.getB() * point.getY() + line.getC();
	}
}
