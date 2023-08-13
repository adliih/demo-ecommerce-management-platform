```sql
INSERT INTO provider ("id", "unique_key", "name")
VALUES (gen_random_uuid (), 'Shopify', 'Shopify')
ON conflict DO NOTHING;
```
