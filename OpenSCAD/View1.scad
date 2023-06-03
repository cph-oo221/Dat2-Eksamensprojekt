union()
{
    linear_extrude(height = 100.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
    {
        scale([2400.0, 6000.0])
        {
            M8();
        }
    }
    union()
    {
        translate([0.0, -3050.0, 0.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([2600.0, 100.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, 3050.0, 0.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([2600.0, 100.0])
                {
                    M8();
                }
            }
        }
        translate([-1250.0, 0.0, 0.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([100.0, 6100.0])
                {
                    M8();
                }
            }
        }
        translate([1250.0, 0.0, 0.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([100.0, 6100.0])
                {
                    M8();
                }
            }
        }
    }
    union()
    {
        translate([0.0, -2710.0, 100.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([2400.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, -2110.0, 100.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([2400.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, -1510.0, 100.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([2400.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, -910.0, 100.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([2400.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, -310.0, 100.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([2400.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, 290.0, 100.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([2400.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, 890.0, 100.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([2400.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, 1490.0, 100.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([2400.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, 2090.0, 100.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([2400.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, 2690.0, 100.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([2400.0, 200.0])
                {
                    M8();
                }
            }
        }
    }
    union()
    {
        translate([-850.0, 0.0, 500.0])
        {
            linear_extrude(height = 550.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([200.0, 6000.0])
                {
                    M8();
                }
            }
        }
        translate([850.0, 0.0, 500.0])
        {
            linear_extrude(height = 550.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([200.0, 6000.0])
                {
                    M8();
                }
            }
        }
    }
    union()
    {
        translate([-850.0, -2500.0, 1050.0])
        {
            linear_extrude(height = 2100.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([200.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([850.0, -2500.0, 1050.0])
        {
            linear_extrude(height = 2100.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([200.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([-850.0, 2500.0, 1050.0])
        {
            linear_extrude(height = 2100.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([200.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([850.0, 2500.0, 1050.0])
        {
            linear_extrude(height = 2100.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([200.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([-850.0, 0.0, 1050.0])
        {
            linear_extrude(height = 2100.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([200.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([850.0, 0.0, 1050.0])
        {
            linear_extrude(height = 2100.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([200.0, 200.0])
                {
                    M8();
                }
            }
        }
    }
}

module M8()
{
    polygon
    (
        points =
        [
            [-0.5, -0.5], 
            [0.5, -0.5], 
            [0.5, 0.5], 
            [-0.5, 0.5]
        ],
        paths =
        [
            [0, 1, 2, 3]
        ]
    );
}
