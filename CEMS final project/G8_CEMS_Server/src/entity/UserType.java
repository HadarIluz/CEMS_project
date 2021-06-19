package entity;

import java.io.Serializable;

/**
 * Enum class was added to save 2 types to differentiate between 3 different
 * types of users in the system, the system differentiates with the help of this
 * class regarding on user permission in the system.
 * 
 * @author Hadar Iluz
 *
 */
public enum UserType implements Serializable {
	Principal, Teacher, Student
}
