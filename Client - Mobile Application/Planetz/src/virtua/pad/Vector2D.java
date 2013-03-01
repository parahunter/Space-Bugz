package virtua.pad;

public class Vector2D implements Cloneable
{
	@Override
	protected Vector2D clone()
	{
		return new Vector2D(this.x, this.y);
	}
	
	public float x;
	public float y;
	
	Vector2D(int xIn, int yIn)
	{
		x = xIn;
		y = yIn;
	}
	
	Vector2D(float xIn, float yIn)
	{
		x = xIn;
		y = yIn;
	}
	
	public Vector2D() {
		x = 0;
		y = 0;
	}
	
	
	
	public Vector2D divideBy(float num)
	{
		Vector2D returnVector = new Vector2D();
		returnVector = (Vector2D)this.clone();
		returnVector.x /= num;
		returnVector.y /= num;
				
		return returnVector;
	}
	
	public Vector2D multiplyBy(float num)
	{
		Vector2D returnVector = new Vector2D();
		returnVector = (Vector2D) this.clone();
		returnVector.x *= num;
		returnVector.y *= num;

		
		return returnVector;
	}
	
	public Vector2D add(Vector2D other)
	{
		Vector2D returnVector = new Vector2D();
		
		returnVector = (Vector2D)this.clone();
		returnVector.x += other.x;
		returnVector.y += other.y;

		return returnVector;
	}
	
	public Vector2D addByReference(Vector2D other)
	{
		Vector2D returnVector = this;
		returnVector.x += other.x;
		returnVector.y += other.y;
		
		return returnVector;
	}
	
	public Vector2D substract(Vector2D other)
	{
		Vector2D returnVector = new Vector2D();
		returnVector = (Vector2D)this.clone();
		returnVector.x -= other.x;
		returnVector.y -= other.y;
		
		
		return returnVector;
	}
	
	public Vector2D truncate(float maxLength)
	{
		float length = this.lengthNonSquared();
		if(length > maxLength*maxLength)
		{
			float scalingFactor = (maxLength*maxLength) / length;
			scalingFactor = (float) Math.sqrt(scalingFactor);
			this.x *= scalingFactor;
			this.y *= scalingFactor;
			return this;
		}
		else
			return this;
	}
	
	public Vector2D normalized()
	{
		float length = lengthSquared();
	
		if(length < 0.000001f)
			return new Vector2D(0.0f, 0.0f);
			
		return this.divideBy(length);
	}
	
	public Vector2D perpendicular()
	{
		Vector2D returnVector = new Vector2D();
		returnVector = (Vector2D) this.clone();
		returnVector.x = - this.y;
		returnVector.y = this.x;
		
		return returnVector;
	}
	
	public float lengthSquared()
	{
		return (float) Math.sqrt(lengthNonSquared());
	}
	
	public float lengthNonSquared()
	{
		return this.x*this.x + this.y*this.y;
	}

	public Vector2D rotated(float radians)
	{
		//float length = this.lengthSquared();
		
		Vector2D returnVector = new Vector2D(0,0);
		
		returnVector.x = (float)(Math.cos(radians)*this.x - Math.sin(radians)*this.y);
		returnVector.y = (float)(-Math.sin(radians)*this.x + Math.cos(radians)*this.x);
		
		return returnVector;
	}
}
