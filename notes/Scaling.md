# Scale Cube Model: X, Y, Z Scaling Explained

## X-Axis Scaling: Cloning Instances
- Multiple identical app instances behind a load balancer.
- Handles more traffic by duplication.
- Best for stateless, simple scaling needs.

## Y-Axis Scaling: Functional Decomposition
- Split app into independent services by business capability.
- Each service is deployed and scaled independently.
- Example services: Order, Payment, Restaurant.

## Z-Axis Scaling: Data Partitioning (Sharding)
- Split application/data by a key (userId, region, etc.).
- Router directs traffic to appropriate instance/shard.
- Improves scalability for large user/data sets.

---

## Visuals

### Scale Cube 3D Overview
![Scale Cube Model](chart:2)

### 2D Scaling Diagrams
![C:\Users\MahmoudHaifawi\IdeaProjects\ecommerce-app\notes\img.png](chart:3)

