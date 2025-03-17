package littleMaidMobX.entity.ai;

public interface IEntityAI {
	
	//実行可能判定
    void setEnable(boolean flag);
	boolean getEnable();
	/**
	 * モードチェンジ実行時に設定される動作状態。
	 */
//	public void setDefaultEnable();

}
