package by.vsu.twoa.domain;

import by.vsu.twoa.geometry.Point;
import by.vsu.twoa.geometry.Triangle;
import by.vsu.twoa.geometry.relpos.PointTriangleRelativePosition;

import java.util.Date;

public class Task extends Entity {
	private User owner;
	private String name;
	private Date created;
	private Triangle triangle;
	private Point point;
	private PointTriangleRelativePosition pointTriangleRelativePosition;

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Triangle getTriangle() {
		return triangle;
	}

	public void setTriangle(Triangle triangle) {
		this.triangle = triangle;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public PointTriangleRelativePosition getPointTriangleRelativePosition() {
		return pointTriangleRelativePosition;
	}

	public void setPointTriangleRelativePosition(PointTriangleRelativePosition pointTriangleRelativePosition) {
		this.pointTriangleRelativePosition = pointTriangleRelativePosition;
	}
}
