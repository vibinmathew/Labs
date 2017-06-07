import React, {Component, PropTypes} from "react";
import "./CategoryList.css";
import CategoryImage from "./CategoryImage"


class CategoryList extends Component {

    render() {
        const {dataCategorization} = this.props;

        this.onCategoryClick = this.onCategoryClick.bind(this);
        return (
            <div>
                <ul className="categories">
                    {this.renderCategoryList(dataCategorization)}
                </ul>
            </div>
        );
    }


    renderCategoryList(dataCategorization) {
        if (dataCategorization && dataCategorization.overall) {

            return dataCategorization.overall.map((catResult) =>
                <li className={catResult.category === dataCategorization.selectedCategory ? 'selected' : ''}
                    key={catResult.category} id={catResult.category}
                    onClick={()=>this.onCategoryClick(catResult.category)}
                >
                    <span className="category-image"><CategoryImage category={catResult.category}/></span>
                    <span className="category-container">{catResult.category}</span>
                    <span className="percentage-container">{catResult.amount}%</span>
                     
                </li>
            );
        }
    }

    onCategoryClick(selectedCategory) {
        this.props.selectCategory(selectedCategory);
    }
}

CategoryList.propTypes = {
    dataCategorization: PropTypes.object
};

export default CategoryList;
