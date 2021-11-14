import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class ledswitch {
    public static void main(String[] args) throws InterruptedException {
        int count = 10;
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "GreenLed", PinState.HIGH);
        led.setShutdownOptions(true, PinState.LOW);
        led.low();

        while (count > 1) {
            System.out.println("LDR Value:");
            System.out.println(rcTime());
            if (rcTime() > 1000000)
            {
                System.out.println("Light is ON");
                led.high();
            }
            else if (rcTime() <= 1000000)
            {
                System.out.println("Light is OFF");
                led.low();
            }

            count++;
            Thread.sleep(500);
        }
        gpio.shutdown();
    }

    public static int rcTime() throws InterruptedException {
        final GpioController gpio = GpioFactory.getInstance();
        int count = 0;
        GpioPinDigitalOutput ldr = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "LdrOut");
        ldr.low();
        Thread.sleep(100);
        gpio.unprovisionPin(ldr);
        GpioPinDigitalInput ldrIn = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, "LdrIn");
        while (ldrIn.isLow())
        {
            count++;
            if (count > 900000)
            {
                gpio.unprovisionPin(ldrIn);
                return count;
            }
        }
        gpio.unprovisionPin(ldrIn);
        return count;
    }
}
