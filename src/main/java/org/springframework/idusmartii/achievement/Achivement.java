package org.springframework.idusmartii.achievement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.idusmartii.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Achivement extends BaseEntity {

	
	@NotNull(message = "Title cant be null")
	@Size(min = 5,max = 50,message = "Title must have between 5 and 20 characters")
	@Column(unique=true)
	private String title;

	@NotNull(message = "Description cant be null")
	@Size(min = 5,max = 50,message = "Description must have between 5 and 50 characters")
	private String description;
	
	@NotNull
	private Condition conditions;
	
	@NotNull(message = "Quantity cant be null")
	@Min(message = "Quantity cant be negative", value = 0)
	private Integer quantity;
	
	@NotNull
	private AplicableEntitys aplicableEntity;
	
}
