package toughasnails.api.temperature;

/**Non-functional**/
public class TemperatureScale
{
    
    public static enum TemperatureRange
    {
        ICY(6),
        COOL(5),
        MILD(4),
        WARM(5),
        HOT(6);
        
        private int rangeSize;
        
        private TemperatureRange(int rangeSize)
        {
            this.rangeSize = rangeSize;
        }
        
        public int getRangeSize()
        {
            return this.rangeSize;
        }
    }
}
