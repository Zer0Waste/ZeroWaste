export interface Product {
    id: number,
    name: string,
    description: string,
    brand: string,
    category: string,
    unitPrice: number,
    promotionPrice: number,
    stock: number,
    expiresAt: Date,
    createdAt: Date,
    updatedAt: Date,
    deletedAt: Date
}