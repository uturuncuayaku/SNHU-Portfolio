# Author: Andres Trujillo
# Date: 9/14/2025
# CS350 Milestone One
# Prof. Gregori

#------------------------------------------------------------------
# Change History
#------------------------------------------------------------------
# Version   |   Description
#------------------------------------------------------------------
#    1          Initial Development
#------------------------------------------------------------------

# These libraries interact with the GPIO using python 
# and the time library slowing our the refresh of our led.

import RPi.GPIO as GPIO
import time

# setting warnings to false for this simple script
# Enabling broadcom mode
# Setting up pin 18 to be the output of this script

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(18, GPIO.OUT)

# Frequency of 60HZ on pin 18
pwm18 = GPIO.PWM(18, 60)

## Start the PWM instance with a 50% duty cycle
#pwm18.start(50)
#
## Wait for input to stop the program
#input('Press return to stop:')
#
## Stop the PWM

#pwm18.stop()
# Starting at zero
pwm18.start(0)

# Repeating until a keyboard breaks our loop
# with our try-catch block, finishing our program
# by properly stopping the device

repeat = True
while repeat:
    try:
        #Lighting up
        for i in range(0, 100, 5):
            pwm18.ChangeDutyCycle(i)
            time.sleep(0.1)

        # Dimming
        for i in range(100, 0, -5):
            pwm18.ChangeDutyCycle(i)
            time.sleep(0.1)

    except KeyboardInterrupt:
        # Stopping the PWM
        print('Stopping PWM and Cleaning Up')
        pwm18.stop()
        GPIO.cleanup()
        repeat = False
# GPIO cleanup
GPIO.cleanup()
