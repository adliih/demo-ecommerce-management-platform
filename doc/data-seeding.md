```sql
INSERT INTO provider ("unique_key", "name")
VALUES ('Shopify', 'Shopify')
ON conflict DO NOTHING;
```
