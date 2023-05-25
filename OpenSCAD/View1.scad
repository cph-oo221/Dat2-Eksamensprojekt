union()
{
    linear_extrude(height = 100.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
    {
        scale([6000.0, 2400.0])
        {
            M8();
        }
    }
    union()
    {
        translate([0.0, -1250.0, 0.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([6200.0, 100.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, 1250.0, 0.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([6200.0, 100.0])
                {
                    M8();
                }
            }
        }
        translate([-3050.0, 0.0, 0.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([100.0, 2500.0])
                {
                    M8();
                }
            }
        }
        translate([3050.0, 0.0, 0.0])
        {
            linear_extrude(height = 400.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([100.0, 2500.0])
                {
                    M8();
                }
            }
        }
    }
    union()
    {
        translate([0.0, -895.0, 100.0])
        {
            linear_extrude(height = 550.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([6000.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, -295.0, 100.0])
        {
            linear_extrude(height = 550.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([6000.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, 305.0, 100.0])
        {
            linear_extrude(height = 550.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([6000.0, 200.0])
                {
                    M8();
                }
            }
        }
        translate([0.0, 905.0, 100.0])
        {
            linear_extrude(height = 550.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([6000.0, 200.0])
                {
                    M8();
                }
            }
        }
    }
    union()
    {
        translate([-2650.0, 0.0, 650.0])
        {
            linear_extrude(height = 550.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([200.0, 2400.0])
                {
                    M8();
                }
            }
        }
        translate([2650.0, 0.0, 650.0])
        {
            linear_extrude(height = 550.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([200.0, 2400.0])
                {
                    M8();
                }
            }
        }
    }
    union()
    {
        translate([-2650.0, -700.0, 1200.0])
        {
            linear_extrude(height = 2100.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([97.0, 97.0])
                {
                    M8();
                }
            }
        }
        translate([2650.0, -700.0, 1200.0])
        {
            linear_extrude(height = 2100.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([97.0, 97.0])
                {
                    M8();
                }
            }
        }
        translate([-2650.0, 700.0, 1200.0])
        {
            linear_extrude(height = 2100.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([97.0, 97.0])
                {
                    M8();
                }
            }
        }
        translate([2650.0, 700.0, 1200.0])
        {
            linear_extrude(height = 2100.0, twist = 0.0, scale = 1.0, slices = 1, center = false)
            {
                scale([97.0, 97.0])
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
