package com.example.boostbonk.data.mock

import com.example.boostbonk.data.model.Post

val mockPosts = listOf(
    Post(
        id = 1,
        user_id = "1",
        username = "@boostbonk",
        description = "Deploying a new DeFi protocol on Solana 🚀 Contracts optimized for low fees.",
        image = "https://example.com/images/bonk1.jpg",
        created_at = "2h ago",
        updated_at = "2h ago",
        boosts = 47,
        bonk_earned = 125_000.0
    ),
    Post(
        id = 2,
        user_id = "2",
        username = "@solana_dev",
        description = "Anchor v0.28 is 🔥 Loving the new macro syntax. Cleaner, faster.",
        image = "https://example.com/images/anchor.jpg",
        created_at = "5h ago",
        updated_at = "5h ago",
        boosts = 12,
        bonk_earned = 10_500.0
    ),
    Post(
        id = 3,
        user_id = "3",
        username = "@bonklabs",
        description = "BONKpay is coming soon. Instant transactions, no fees. Stay tuned!",
        image = "https://example.com/images/bonkpay.png",
        created_at = "8h ago",
        updated_at = "8h ago",
        boosts = 31,
        bonk_earned = 76_320.0
    ),
    Post(
        id = 4,
        user_id = "4",
        username = "@nft_maniac",
        description = "Dropped a new NFT collection — BONKapes 🐵. Minting now!",
        image = "https://example.com/images/nftapes.jpg",
        created_at = "14h ago",
        updated_at = "14h ago",
        boosts = 64,
        bonk_earned = 203_900.0
    ),
    Post(
        id = 5,
        user_id = "5",
        username = "@dev_dani",
        description = "Built a Solana faucet dApp in one night 💧 Open source and live!",
        image = null,
        created_at = "1d ago",
        updated_at = "1d ago",
        boosts = 20,
        bonk_earned = 58_000.0
    ),
    Post(
        id = 6,
        user_id = "6",
        username = "@stakingqueen",
        description = "Yield farming with BONK pairs 💰 APR is looking juicy.",
        image = "https://example.com/images/yield.jpg",
        created_at = "2d ago",
        updated_at = "2d ago",
        boosts = 83,
        bonk_earned = 312_400.0
    )
)
